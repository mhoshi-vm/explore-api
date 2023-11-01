package co.jp.vmware.tanzu.explore.api.config;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.StringReader;


@Component
public class MyWfSpanConfig {

    public final String dbType;

    public final String dbInstance;

    public final String appName;

    public final String inboundServiceType;

    Logger logger = LoggerFactory.getLogger(MyWfSpanConfig.class);

    CCJSqlParserManager ccjSqlParserManager;

    TablesNamesFinder tablesNamesFinder;

    Statement statement;

    public MyWfSpanConfig(@Value("${db.type:VectorDB}") String dbType, @Value("${db.instance:Greenplum DB}") String dbInstance,
                          @Value("${app.name}") String appName,
                          @Value("${inboundExternalService.serviceType:Frontend}") String inboundServiceType) {
        this.dbType = dbType;
        this.dbInstance = dbInstance;
        this.appName = appName;
        this.ccjSqlParserManager = new CCJSqlParserManager();
        this.tablesNamesFinder = new TablesNamesFinder();
        this.inboundServiceType = inboundServiceType;
    }

    @Bean
    SpanHandler spanDebugHandler() {

        return new SpanHandler() {
            @Override
            public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
                logger.debug("New Span!!");
                logger.debug("Span name : " + span.name());
                logger.debug("Span kind : " + span.kind());
                logger.debug("Span Remote Source :" + span.remoteServiceName());
                span.tags().forEach((key, value) -> logger.debug("     tag :" + key + " value: " + value));
                return true;
            }
        };
    }

    @Bean
    SpanHandler spanServiceHandler() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {

                for (int i = 0; i < span.tagCount(); i++) {
                    if (span.tagKeyAt(i).startsWith("jdbc.query")) {
                        try {
                            statement = ccjSqlParserManager.parse(new StringReader(span.tagValueAt(i)));
                            logger.debug("Sending trace to table :" + tablesNamesFinder.getTables(statement).iterator().next());
                        } catch (JSQLParserException e) {
                            logger.warn("Unable to parse SQL");
                            return false;
                        }
                        span.tag("_outboundExternalService", tablesNamesFinder.getTables(statement).iterator().next());
                        span.tag("_externalApplication", appName);
                        span.tag("_externalComponent", dbType);
                    }
                }

                return true;
            }
        };
    }

    @Bean
    SpanHandler spanServletHandler() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
                if (span.kind() != null && span.kind().name().equals("SERVER")) {
                    for (int i = 0; i < span.tagCount(); i++) {
                        if (span.tagKeyAt(i).startsWith("http.url")) {
                            span.tag("_inboundExternalService", inboundServiceType);
                            span.tag("_externalApplication", appName);
                            span.tag("_externalComponent", inboundServiceType);
                        }
                    }
                }

                return true;
            }
        };
    }

}
