package co.jp.vmware.tanzu.explore.api.service;

import co.jp.vmware.tanzu.explore.api.record.Summary;
import co.jp.vmware.tanzu.explore.api.repository.SummaryRepo;
import org.springframework.ai.autoconfigure.openai.OpenAiProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class SummaryService {

    String embeddingModels;

    SummaryRepo summaryRepo;

    JdbcTemplate jdbcTemplate;

    public SummaryService(SummaryRepo summaryRepo, JdbcTemplate jdbcTemplate, OpenAiProperties openAiProperties) {
        this.embeddingModels = openAiProperties.getEmbeddingModel();
        this.summaryRepo = summaryRepo;
        this.jdbcTemplate = jdbcTemplate;
    }

    public static class SummaryMapper implements RowMapper<Summary> {

        @Override
        public Summary mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Summary(rs.getInt("id"), rs.getString("session_id"), rs.getString("title"), rs.getString("content"));
        }

    }

    public List<Summary> get(String prompt, Integer limit) {
        SummaryMapper recordMapper = new SummaryMapper();
        System.out.println(this.embeddingModels);
        return this.jdbcTemplate.query(
                "SELECT DISTINCT ON (distance) id, session_id, title, content, pgml.embed(?, ?)::vector <-> embeddings AS distance FROM summary ORDER BY distance LIMIT ?",
                recordMapper, this.embeddingModels, prompt, limit);
    }

    public List<Summary> get() {
        // Do Vector Search
        return summaryRepo.findAll();
    }
}
