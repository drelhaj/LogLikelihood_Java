package com.stats;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class WordCloudCreator {
	protected int wordCount;
	protected int corpusSize;
	protected static int cloudType;

	private static PrintWriter writer, writer2;

	public WordCloudCreator() {

	}

	public WordCloudCreator(int wordCount, int corpusSize) {

		this.wordCount = wordCount;
		this.corpusSize = corpusSize;
	}

	public static void LLH_WordCloud(String src, String ref, String keywordsFile, String userOutputFile, int type)
			throws IOException {
		WordCloudCreator llh = new WordCloudCreator(type);
		llh.setCloudType(type);
		String csvFile = "llhCloud/data.csv";

		writer = new PrintWriter(csvFile, "UTF-8");
		writer.println("text" + "," + "size");
		writer.flush();

		if (userOutputFile.length() > 1) {
			writer2 = new PrintWriter(userOutputFile, "UTF-8");
			writer2.println("text" + "," + "size" + "," + "Freq");
			writer2.flush();
		}

		String[] keywords = FileToArrayReader.readLines(keywordsFile);

		for (String keyword : keywords) {

			WordCloudCreator llhRef = WordFrequencyCounter.countWordFreq(keyword, Paths.get(ref));
			WordCloudCreator llhSrc = WordFrequencyCounter.countWordFreq(keyword, Paths.get(src));

			double logLL = LlhCalculator.logLikeliHood(llhSrc.wordCount, llhRef.wordCount, llhSrc.corpusSize,
					llhRef.corpusSize);
			System.out.println("LLH of: " + keyword + " = " + logLL);
			writer.println(keyword + "," + logLL + "_" + llhSrc.wordCount);
			writer.flush();
			if (userOutputFile.length() > 1) {
				writer2.println(keyword + "," + logLL + "," + llhSrc.wordCount);
				writer2.flush();
			}
		}

		writer.close();
		LLH_Interface.main(type);

	}

	public WordCloudCreator(int cloudType) {
		WordCloudCreator.cloudType = cloudType;
	}

	public int getWordCount() {
		return wordCount;
	}

	public int getCorpusSizse() {
		return corpusSize;
	}

	public int getCloudType() {
		return cloudType;
	}

	public void setCloudType(int type) {
		WordCloudCreator.cloudType = type;
	}

}