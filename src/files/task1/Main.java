package files.task1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main {
	private static StringBuilder log = new StringBuilder();

	public static void main(String[] args) {
		boolean isCreated;
		String root = "E:/temp/Games"; // must be created manually
		try {
			isCreated = createDirs(root, "src", "res", "savegames", "temp");
			check(isCreated);

			isCreated = createDirs(root + "/src", "main", "test");
			check(isCreated);

			isCreated = createFiles(root + "/src/main", "Main.java", "Utils.java");
			check(isCreated);

			isCreated = createDirs(root + "/res", "drawables", "vectors", "icons");
			check(isCreated);

			isCreated = createFiles(root + "/temp", "temp.txt");
			check(isCreated);
		} catch (IDKException e) {
			log("Не удалось создать папку или файл, логи:");
			System.out.println(log);
			System.out.println("Выход...");
			return;
		}

		try {
			Files.writeString(Path.of(root + "/temp/temp.txt"), log.toString());
			System.out.println("Логи записаны в файл " + root + "/temp/temp.txt");
		} catch (IOException e) {
			System.out.println("Не удалось сохранить логи, вывод в stdout...");
			System.out.println(log);
		}
	}

	private static void check(boolean isCreatedThings) throws IDKException {
		if (!isCreatedThings) throw new IDKException();
	}

	private static boolean createFiles(String root, String... files) {
		for (String file : files) {
			boolean mkdir;
			try {
				mkdir = new File(root + "/" + file).createNewFile();
			} catch (IOException e) {
				mkdir = false;
			}
			if (mkdir) {
				log("Создан файл " + file);
			} else {
				log("Не удалось создать файл " + file);
				return false;
			}
		}
		return true;
	}

	private static boolean createDirs(String root, String... dirs) {
		for (String dir : dirs) {
			boolean mkdir = new File(root + "/" + dir).mkdir();
			if (mkdir) {
				log("Создана папка " + dir);
			} else {
				log("Не удалось создать папку " + dir);
				return false;
			}
		}
		return true;
	}

	private static void log(String someText) {
		log.append(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
				.append(" - ")
				.append(someText)
				.append('\n');
	}

	private static class IDKException extends Exception {
	}
}
