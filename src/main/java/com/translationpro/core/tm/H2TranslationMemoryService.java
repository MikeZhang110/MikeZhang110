package com.translationpro.core.tm;

import com.google.inject.Singleton;
import com.translationpro.TranslationProApp;
import com.translationpro.util.FuzzyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * H2 database-backed implementation of the translation memory service.
 *
 * This implementation provides fast, scalable translation memory storage
 * with advanced fuzzy matching capabilities.
 */
@Singleton
public class H2TranslationMemoryService implements TranslationMemoryService {

    private static final Logger logger = LoggerFactory.getLogger(H2TranslationMemoryService.class);

    private Connection connection;
    private final FuzzyMatcher fuzzyMatcher;

    public H2TranslationMemoryService() {
        this.fuzzyMatcher = new FuzzyMatcher();
        initialize();
    }

    @Override
    public void initialize() {
        try {
            String dbPath = TranslationProApp.getProperties()
                    .getProperty("db.path", "./data/translationpro");

            String url = "jdbc:h2:" + dbPath + ";AUTO_SERVER=TRUE";
            connection = DriverManager.getConnection(url, "sa", "");

            logger.info("Connected to H2 database at: {}", dbPath);

            createTables();
            createIndexes();

        } catch (SQLException e) {
            logger.error("Failed to initialize translation memory database", e);
            throw new RuntimeException("Failed to initialize TM database", e);
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Translation units table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS translation_units (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    source_text TEXT NOT NULL,
                    target_text TEXT NOT NULL,
                    source_lang VARCHAR(10) NOT NULL,
                    target_lang VARCHAR(10) NOT NULL,
                    source_hash VARCHAR(64) NOT NULL,
                    creation_date TIMESTAMP NOT NULL,
                    modification_date TIMESTAMP,
                    creator VARCHAR(255),
                    project_name VARCHAR(255),
                    subject_field VARCHAR(100),
                    client VARCHAR(255),
                    quality_score TINYINT DEFAULT 0,
                    usage_count INT DEFAULT 0
                )
            """);

            // Context segments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS segments (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    tu_id BIGINT NOT NULL,
                    previous_source TEXT,
                    next_source TEXT,
                    document_name VARCHAR(255),
                    segment_number INT,
                    FOREIGN KEY (tu_id) REFERENCES translation_units(id) ON DELETE CASCADE
                )
            """);

            logger.info("Database tables created/verified");
        }
    }

    private void createIndexes() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_source_hash ON translation_units(source_hash)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_langs ON translation_units(source_lang, target_lang)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_creation_date ON translation_units(creation_date)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_project ON translation_units(project_name)");

            logger.info("Database indexes created/verified");
        }
    }

    @Override
    public TranslationUnit addTranslationUnit(TranslationUnit tu) {
        String sql = """
            INSERT INTO translation_units
            (source_text, target_text, source_lang, target_lang, source_hash,
             creation_date, creator, project_name, subject_field, client, quality_score)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tu.getSourceText());
            pstmt.setString(2, tu.getTargetText());
            pstmt.setString(3, tu.getSourceLang());
            pstmt.setString(4, tu.getTargetLang());
            pstmt.setString(5, tu.getSourceHash());
            pstmt.setTimestamp(6, Timestamp.valueOf(tu.getCreationDate()));
            pstmt.setString(7, tu.getCreator());
            pstmt.setString(8, tu.getProjectName());
            pstmt.setString(9, tu.getSubjectField());
            pstmt.setString(10, tu.getClient());
            pstmt.setInt(11, tu.getQualityScore());

            pstmt.executeUpdate();

            // Get generated ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tu.setId(rs.getLong(1));
                }
            }

            // Add context if available
            if (tu.getPreviousSource() != null || tu.getNextSource() != null) {
                addContext(tu);
            }

            logger.debug("Added translation unit: {}", tu);
            return tu;

        } catch (SQLException e) {
            logger.error("Failed to add translation unit", e);
            throw new RuntimeException("Failed to add translation unit", e);
        }
    }

    private void addContext(TranslationUnit tu) throws SQLException {
        String sql = """
            INSERT INTO segments
            (tu_id, previous_source, next_source, document_name, segment_number)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, tu.getId());
            pstmt.setString(2, tu.getPreviousSource());
            pstmt.setString(3, tu.getNextSource());
            pstmt.setString(4, tu.getDocumentName());
            pstmt.setInt(5, tu.getSegmentNumber());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateTranslationUnit(TranslationUnit tu) {
        String sql = """
            UPDATE translation_units
            SET target_text = ?, modification_date = ?, quality_score = ?, usage_count = ?
            WHERE id = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tu.getTargetText());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, tu.getQualityScore());
            pstmt.setInt(4, tu.getUsageCount());
            pstmt.setLong(5, tu.getId());

            pstmt.executeUpdate();
            logger.debug("Updated translation unit: {}", tu.getId());

        } catch (SQLException e) {
            logger.error("Failed to update translation unit", e);
            throw new RuntimeException("Failed to update translation unit", e);
        }
    }

    @Override
    public void deleteTranslationUnit(Long id) {
        String sql = "DELETE FROM translation_units WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            logger.debug("Deleted translation unit: {}", id);

        } catch (SQLException e) {
            logger.error("Failed to delete translation unit", e);
            throw new RuntimeException("Failed to delete translation unit", e);
        }
    }

    @Override
    public TranslationUnit getExactMatch(String sourceText, String sourceLang, String targetLang) {
        String sql = """
            SELECT * FROM translation_units
            WHERE source_hash = ? AND source_lang = ? AND target_lang = ?
            ORDER BY modification_date DESC NULLS LAST, creation_date DESC
            LIMIT 1
        """;

        String hash = Integer.toHexString(sourceText.hashCode());

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, hash);
            pstmt.setString(2, sourceLang);
            pstmt.setString(3, targetLang);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbSourceText = rs.getString("source_text");
                    // Verify exact match (hash collision check)
                    if (sourceText.equals(dbSourceText)) {
                        return mapResultSetToTU(rs);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to get exact match", e);
        }

        return null;
    }

    @Override
    public List<FuzzyMatch> searchFuzzyMatches(String sourceText, String sourceLang, String targetLang,
                                                int threshold, int maxResults) {
        return searchFuzzyMatchesWithContext(sourceText, null, null, sourceLang, targetLang, threshold, maxResults);
    }

    @Override
    public List<FuzzyMatch> searchFuzzyMatchesWithContext(String sourceText, String previousSource,
                                                           String nextSource, String sourceLang, String targetLang,
                                                           int threshold, int maxResults) {
        List<FuzzyMatch> matches = new ArrayList<>();

        // First, check for exact match
        TranslationUnit exactMatch = getExactMatch(sourceText, sourceLang, targetLang);
        if (exactMatch != null) {
            matches.add(new FuzzyMatch(exactMatch, 100, FuzzyMatch.MatchType.EXACT));
            if (matches.size() >= maxResults) {
                return matches;
            }
        }

        // Search for fuzzy matches
        String sql = """
            SELECT * FROM translation_units
            WHERE source_lang = ? AND target_lang = ?
            ORDER BY modification_date DESC NULLS LAST, creation_date DESC
            LIMIT ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sourceLang);
            pstmt.setString(2, targetLang);
            pstmt.setInt(3, maxResults * 10); // Get more candidates for fuzzy matching

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next() && matches.size() < maxResults) {
                    TranslationUnit tu = mapResultSetToTU(rs);

                    // Skip exact matches (already added)
                    if (sourceText.equals(tu.getSourceText())) {
                        continue;
                    }

                    // Calculate fuzzy match score
                    int score = fuzzyMatcher.calculateSimilarity(sourceText, tu.getSourceText());

                    if (score >= threshold) {
                        FuzzyMatch.MatchType matchType = determineMatchType(score);
                        matches.add(new FuzzyMatch(tu, score, matchType));
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to search fuzzy matches", e);
        }

        // Sort by score (descending)
        matches.sort(FuzzyMatch::compareTo);

        // Limit to maxResults
        if (matches.size() > maxResults) {
            matches = matches.subList(0, maxResults);
        }

        logger.debug("Found {} fuzzy matches for: {}", matches.size(), sourceText);
        return matches;
    }

    private FuzzyMatch.MatchType determineMatchType(int score) {
        if (score >= 90) return FuzzyMatch.MatchType.FUZZY_HIGH;
        if (score >= 75) return FuzzyMatch.MatchType.FUZZY_MEDIUM;
        return FuzzyMatch.MatchType.FUZZY_LOW;
    }

    private TranslationUnit mapResultSetToTU(ResultSet rs) throws SQLException {
        TranslationUnit tu = new TranslationUnit();
        tu.setId(rs.getLong("id"));
        tu.setSourceText(rs.getString("source_text"));
        tu.setTargetText(rs.getString("target_text"));
        tu.setSourceLang(rs.getString("source_lang"));
        tu.setTargetLang(rs.getString("target_lang"));
        tu.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());

        Timestamp modDate = rs.getTimestamp("modification_date");
        if (modDate != null) {
            tu.setModificationDate(modDate.toLocalDateTime());
        }

        tu.setCreator(rs.getString("creator"));
        tu.setProjectName(rs.getString("project_name"));
        tu.setSubjectField(rs.getString("subject_field"));
        tu.setClient(rs.getString("client"));
        tu.setQualityScore(rs.getInt("quality_score"));
        tu.setUsageCount(rs.getInt("usage_count"));

        return tu;
    }

    @Override
    public int importFromTMX(String tmxFilePath) {
        // TODO: Implement TMX import
        logger.info("TMX import not yet implemented");
        return 0;
    }

    @Override
    public int exportToTMX(String tmxFilePath, String sourceLang, String targetLang) {
        // TODO: Implement TMX export
        logger.info("TMX export not yet implemented");
        return 0;
    }

    @Override
    public TMStatistics getStatistics() {
        TMStatistics stats = new TMStatistics();

        try (Statement stmt = connection.createStatement()) {
            // Total translation units
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM translation_units")) {
                if (rs.next()) {
                    stats.setTotalTranslationUnits(rs.getLong(1));
                }
            }

            // Language pair counts
            Map<String, Long> langPairCounts = new HashMap<>();
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT source_lang, target_lang, COUNT(*) FROM translation_units GROUP BY source_lang, target_lang")) {
                while (rs.next()) {
                    String key = rs.getString(1) + "-" + rs.getString(2);
                    langPairCounts.put(key, rs.getLong(3));
                }
            }
            stats.setLanguagePairCounts(langPairCounts);

            // Project counts
            Map<String, Long> projectCounts = new HashMap<>();
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT project_name, COUNT(*) FROM translation_units WHERE project_name IS NOT NULL GROUP BY project_name")) {
                while (rs.next()) {
                    projectCounts.put(rs.getString(1), rs.getLong(2));
                }
            }
            stats.setProjectCounts(projectCounts);

        } catch (SQLException e) {
            logger.error("Failed to get statistics", e);
        }

        return stats;
    }

    @Override
    public void clear() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM segments");
            stmt.execute("DELETE FROM translation_units");
            logger.info("Translation memory cleared");

        } catch (SQLException e) {
            logger.error("Failed to clear translation memory", e);
            throw new RuntimeException("Failed to clear TM", e);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Translation memory database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection", e);
        }
    }
}
