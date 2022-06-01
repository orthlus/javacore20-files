package files.task3;

import files.task2.GameProgress;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
	public static void main(String[] args) throws IOException {
		String dir = "E:/temp/Games/savegames";
		Optional<Path> zipPath = findFirstFileWithExt(dir, ".zip");
		if (zipPath.isEmpty()) {
			System.out.println("Не найден zip файл");
			return;
		}
		openZip(zipPath.get().toString(), dir);
		Optional<Path> gameProcessPath = findFirstFileWithExt(dir, ".dat");
		if (gameProcessPath.isEmpty()) {
			System.out.println("Не найден dat файл");
			return;
		}
		System.out.println("Объект GameProgress из файла:");
		System.out.println(openProgress(gameProcessPath.get().toString()));
	}

	private static Optional<Path> findFirstFileWithExt(String dir, String extension) throws IOException {
		try (Stream<Path> paths = Files.walk(Path.of(dir))) {
			return paths.filter(file -> file.getFileName().toString().endsWith(extension)).findFirst();
		}
	}

	private static void openZip(String zipFile, String dir) {
		try (var fileIS = new FileInputStream(zipFile);
			 var zipIS = new ZipInputStream(fileIS)) {
			ZipEntry zipEntry = zipIS.getNextEntry();
			while (zipEntry != null) {
				try (var fileOS = new FileOutputStream(dir + "/" + zipEntry.getName())) {
					fileOS.write(zipIS.readAllBytes());
				}
				zipIS.closeEntry();
				zipEntry = zipIS.getNextEntry();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static GameProgress openProgress(String fileName) {
		try (var fileIS = new FileInputStream(fileName);
			 var objectIS = new ObjectInputStream(fileIS)) {
			Object o = objectIS.readObject();
			return (GameProgress) o;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
