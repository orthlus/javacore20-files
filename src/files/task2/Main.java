package files.task2;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
	static Supplier<String> now = () -> LocalDateTime.now()
			.truncatedTo(ChronoUnit.SECONDS)
			.toString()
			.replaceAll(":", "-");
	public static void main(String[] args) {
		String dir = "E:/temp/Games/savegames";
		GameProgress gameProgress1 = new GameProgress(100, 30, 1, 20.3);
		GameProgress gameProgress2 = new GameProgress(80, 45, 10, 15.44);
		GameProgress gameProgress3 = new GameProgress(200, 8, 4, 0.6);
		saveGame(dir + "/game1.dat", gameProgress1);
		saveGame(dir + "/game2.dat", gameProgress2);
		saveGame(dir + "/game3.dat", gameProgress3);
		zipFiles(dir + "/saves-%s.zip".formatted(now.get()),
				dir + "/game1.dat",
				dir + "/game2.dat",
				dir + "/game3.dat");
	}

	private static void saveGame(String fileName, GameProgress object) {
		try (var file = new FileOutputStream(fileName);
			 var stream = new ObjectOutputStream(file)) {
			stream.writeObject(object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void zipFiles(String zipFileName, String... files) {
		try (var fileOS = new FileOutputStream(zipFileName);
			 var zipOS = new ZipOutputStream(fileOS)) {
			for (String fileAbsolute : files) {
				String fileName = Path.of(fileAbsolute).getFileName().toString();
				zipOS.putNextEntry(new ZipEntry(fileName));
				try (var fileIS = new FileInputStream(fileAbsolute)) {
					zipOS.write(fileIS.readAllBytes());
				}
				zipOS.closeEntry();
			}
			for (String file : files) {
				new File(file).delete();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
