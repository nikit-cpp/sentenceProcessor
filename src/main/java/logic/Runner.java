package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner {
	@Value("${nThreads}")
	int nThreads;
	
	@Value("${wordCount}")
	int wordCount;
	
	@Value("${file}")
	String file;
	
	@Value("${file-encoding}")
	String fileEncoding;

    static String exit="q";
	
	/**
	 * сам себе лаунчер :)
	 * @param args
	 */
	public static void main(String args[]) throws IOException {

        ClassPathXmlApplicationContext context= new ClassPathXmlApplicationContext(
                "SpringConfig.xml");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String isExit="";
        do {
            // Когда для автоматического поиска бины помечены как компоненты, в
            // методе getBean в качестве имени выступает имя класса с маленькой
            // буквы, по соглашению
            Runner obj = (Runner) context.getBean("runner");
            obj.run();

            System.out.println("Enter \"" + exit +"\" to exit, or enter any other to reload properties and re-process file...");

            isExit = bufferedReader.readLine();
            context.refresh();
        }while (!isExit.equals(exit));

        bufferedReader.close();
		context.close();
	}
	
	void run(){
		ExecutorService service = Executors.newFixedThreadPool(nThreads);

		try {
			org.joda.time.DateTime startTime = new org.joda.time.DateTime();

			List<String> strings = readSentences(
					new File(file), fileEncoding);



			// Разделяем предложения на слова
			List<Callable<ResultContainer>> wordSplitTasks = new ArrayList<Callable<ResultContainer>>();

			int i = 0;
			for (final String s : strings) {
				final int iii = i;
				wordSplitTasks.add(new Callable<ResultContainer>() {
					public ResultContainer call() throws Exception {
						int count = 0;
						List<String> a = split2(s);
						for (int i = 0; i < a.size(); i++) {
							if (a.get(i) != null) {
								count++;
							}
						}
						// System.out.println("Обрабатываю \"" + s+ "\" "+count
						// + "слов");
						if (count == wordCount) {
							// Если строка подходит, то возвращаем её
							return new ResultContainer(s, iii);
						} else {
							return new ResultContainer(null, iii);
						}
					}
				});
				++i;
			}

			List<Future<ResultContainer>> futures = service
					.invokeAll(wordSplitTasks);

			List<ResultContainer> results = new ArrayList<ResultContainer>();

			// В future -- результаты предложения
			for (Future<ResultContainer> frc : futures) {
				ResultContainer rc = frc.get();
				results.add(rc);
			}
			
			Collections.sort(results);

			// В future -- результаты предложения
			for (ResultContainer rc : results) {
				if (rc.s != null)
					System.out.println(rc.s);
			}
			
			org.joda.time.DateTime endTime = new org.joda.time.DateTime();

			System.out.println("\nprinted sentences with: " + wordCount
					+ " words");
			System.out.println("threads: " + nThreads);
			System.out.println("wasted time: "
					+ (endTime.getMillis() - startTime.getMillis()));
			//

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}finally{
			service.shutdown();
		}
	}

	public static String ruRegex = "[а-яА-Яa-zA-Z]+";

	static List<String> split2(String s) {
		Pattern p = Pattern.compile(ruRegex);
		Matcher m = p.matcher(s);

		List<String> ls = new ArrayList<String>();

		for (; m.find();) {
			String u = m.group();
			ls.add(u);
		}
		return ls;
	}

	private static List<String> readSentences(File file, String charset)
			throws IOException {
		InputStream is = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				charset));

		List<String> list = new ArrayList<String>();
		int c = 0;
		StringBuilder sb = new StringBuilder();
		while ((c = br.read()) != -1) {
			char ch = (char) c;
			if (ch == '\r' || ch == '\n')
				continue;
			sb.append(ch);
			if (ch == '.') {
				String s = sb.toString();
				// System.out.println(s);
				list.add(s);
				sb.setLength(0);
			}

		}
		is.close();
		br.close();
		return list;
	}
}

class ResultContainer implements Comparable<ResultContainer> {
	public ResultContainer(String s, int position) {
		super();
		this.s = s;
		this.position = position;
	}

	public String s;
	public int position;

	public int compareTo(ResultContainer o) {
		int compareQuantity = o.position;

		// ascending order
		return position - compareQuantity;
	}
}
