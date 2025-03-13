package dev.emircankirez.clean_code_assistant.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DocsLoader {

    @Value("classpath:/docs/clean-code.pdf")
    private Resource cleanCodePdf;
    
    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = getVectorStoreFile();

        if (vectorStoreFile.exists()) {
            simpleVectorStore.load(vectorStoreFile);
        } else {
            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageTopMargin(0)
                    .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                            .withNumberOfTopTextLinesToDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();
            PagePdfDocumentReader reader = new PagePdfDocumentReader(cleanCodePdf, config);
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            simpleVectorStore.accept(tokenTextSplitter.apply(reader.get()));
            simpleVectorStore.save(vectorStoreFile);
        }

        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + File.separator + "vectorstore.json";
        return new File(absolutePath);
    }
}

