package com.wongnai.tools.de.cmd;

import java.io.FileInputStream;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

import com.wongnai.tools.de.service.DataExporter;
import com.wongnai.tools.de.settings.Settings;
import com.wongnai.tools.de.spring.SpringConfig;

/**
 * Application's command-line runner.
 *
 * @author Suparit Krityakien
 */
public class Application implements ApplicationRunner {
	private DataExporter dataExporter;

	/**
	 * Sets data exporter.
	 *
	 * @param dataExporter
	 *            data exporter
	 */
	public void setDataExporter(DataExporter dataExporter) {
		this.dataExporter = dataExporter;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!args.getNonOptionArgs().isEmpty()) {
			String cmd = args.getNonOptionArgs().get(0);

			if ("export".equals(cmd)) {
				dataExporter.export(Settings.fromYaml(new FileInputStream(args.getNonOptionArgs().get(1))));
			} else if ("printConfig".equals(cmd)) {
				dataExporter.printConfig(System.out);
			} else {
				printUsage();
			}
		} else {
			printUsage();
		}
	}

	private void printUsage() {
		System.out.println();
		System.out.println();
		System.out.println("-----===== Relation Data Exporter =====-----");
		System.out.println("Usage : java [JAVA_OPTS] -jar <app.jar> <command> [parameters]");
		System.out.println();
		System.out.println("available commands:- ");
		System.out.println("* export <config.yaml>");
		System.out.println("	to export data using given configuration");
		System.out.println("* printConfig");
		System.out.println("	to print example configuration yaml based on current database");
		System.out.println();
		System.out.println("available java options:- ");
		System.out.println("* spring.datasource.url");
		System.out.println("	jdbc connection"
				+ " e.g. jdbc:mysql://localhost:3306/wongnai?useUnicode=true&characterEncoding=UTF-8");
		System.out.println("* spring.datasource.username");
		System.out.println("	db username");
		System.out.println("* spring.datasource.password");
		System.out.println("	db password");
		System.out.println();
		System.out.println("Example : java"
				+ " -Dspring.datasource.url=jdbc:mysql://localhost:3306/wongnai?useUnicode=true&characterEncoding=UTF-8"
				+ " -Dspring.datasource.username=usr" + " -Dspring.datasource.password=pwd"
				+ " -jar de-0.1.jar export mydb.yaml");
		System.out.println();
		System.out.println();
	}

	/**
	 * The legendary main entry.
	 *
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringConfig.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
