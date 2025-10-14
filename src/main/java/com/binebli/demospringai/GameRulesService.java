package com.binebli.demospringai;

import static org.springframework.ai.vectorstore.filter.Filter.ExpressionType.EQ;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Service;

@Service
public class GameRulesService {
  private final VectorStore vectorStore;

  public GameRulesService(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  private static final Logger LOG = LoggerFactory.getLogger(GameRulesService.class);

  public String getRulesFor(String gameName, String question) {
    SearchRequest searchRequest =
        SearchRequest.builder()
            .query(question)
//            .filterExpression(
//                new FilterExpressionBuilder().eq("gameTitle", gameName).build())
                        .filterExpression(
                                new Filter.Expression(
                                    EQ,
                                    new Filter.Key("gameTitle"),
                                    new Filter.Value(normalizeGameTitle(gameName))))
            // As a consequence, the search will return at most 6 similar documents.
            .topK(6)
            .build();
    System.err.println("Search request: " + searchRequest);
    List<Document> similarDocs = vectorStore.similaritySearch(searchRequest); // #2
    if (similarDocs.isEmpty()) {
      return "The rules for " + gameName + " are not available.";
    }
    return similarDocs.stream()
        .map(Document::getText)
        .collect(Collectors.joining(System.lineSeparator())); // #3
  }

  String normalizeGameTitle(String gameTitle) { // #4
    return gameTitle
        .toLowerCase()
        .replace(" ", "_"); //      SearchRequest searchRequest = SearchRequest
    //              .builder()
    //              .query(question)
    //              .build();
    // .withFilterExpression(
    // new FilterExpressionBuilder()
    // .eq("gameTitle", normalizeGameTitle(gameName)).build());
  }
}
