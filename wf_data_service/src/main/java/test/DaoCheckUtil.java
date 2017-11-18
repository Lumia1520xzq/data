package test;
import com.wf.core.persistence.MyBatisDao;
import com.wf.core.utils.type.StringUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Dao类检查工具
 * @author zwf
 */
public class DaoCheckUtil {

	public static final String PACKAGE_NAME = "com.wf.data.dao.uic";

	public static void main(String[] args) throws Exception {
		List<String> classNames = getClassName(PACKAGE_NAME, false);
		if (classNames == null) {
			System.out.println("no class found.");
			System.exit(0);
		}

		List<String> errorMyBatisDaoClasses = new ArrayList<>();
		for (String className : classNames) {
			Class cls = Class.forName(className);
			String simpleName = cls.getSimpleName();
			if (simpleName.length() <= 3 || !simpleName.substring(simpleName.length() - 3).equalsIgnoreCase("dao")) {
				errorMyBatisDaoClasses.add(className);
				continue;
			}

			Annotation annotation = cls.getAnnotation(MyBatisDao.class);
			if (annotation == null) {
				errorMyBatisDaoClasses.add(className);
				continue;
			}
			MyBatisDao myBatisDao = (MyBatisDao) annotation;
			String tableName = myBatisDao.tableName();
			if (StringUtils.isBlank(tableName)) {
				errorMyBatisDaoClasses.add(className);
				continue;
			}
			if (!tableName.contains("_")) {
				errorMyBatisDaoClasses.add(className);
				continue;
			}
			StringBuilder name = new StringBuilder();
			String[] tableNameArray = tableName.split("_");
			for (String s : tableNameArray) {
				for (int i = 0; i < s.length(); i++) {
					if (i == 0) {
						name.append(Character.toUpperCase(s.charAt(i)));
					} else {
						name.append(Character.toLowerCase(s.charAt(i)));
					}
				}
			}
			if (!name.toString().equals(simpleName.substring(0, simpleName.length() - 3))) {//remove 'dao' or 'DAO'
				errorMyBatisDaoClasses.add(className);
			}
		}
		if (!errorMyBatisDaoClasses.isEmpty()) {
			System.out.println("错误的DAO类：");
			for (String s : errorMyBatisDaoClasses) {
				System.out.println(s);
			}
		} else {
			System.out.println("没有找到错误的DAO类");
		}
	}

	/**
	 * 获取某包下（包括该包的所有子包）所有类
	 * @param packageName 包名
	 * @return 类的完整名称
	 * @throws Exception
	 */
	public static List<String> getClassName(String packageName) throws Exception {
		return getClassName(packageName, true);
	}

	/**
	 * 获取某包下所有类
	 * @param packageName 包名
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 * @throws Exception
	 */
	public static List<String> getClassName(String packageName, boolean childPackage) throws Exception {
		List<String> fileNames = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String packagePath = packageName.replace(".", "/");
		URL url = loader.getResource(packagePath);
		if (url != null) {
			String type = url.getProtocol();
			if ("file".equals(type)) {
				fileNames = getClassNameByFile(url.getPath(), null, childPackage);
			} else if (("jar").equals(type)) {
				fileNames = getClassNameByJar(url.getPath(), childPackage);
			}
		} else {
			fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);
		}
		return fileNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 * @param filePath 文件路径
	 * @param className 类名集合
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
				}
			} else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(".class")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
					childFilePath = childFilePath.replace("\\", ".");
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
	}

	/**
	 * 从jar获取某包下所有类
	 * @param jarPath jar文件路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 * @throws Exception
	 */
	private static List<String> getClassNameByJar(String jarPath, boolean childPackage) throws Exception {
		List<String> myClassName = new ArrayList<String>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
		String packagePath = jarInfo[1].substring(1);
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					} else {
						int index = entryName.lastIndexOf("/");
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						if (myPackagePath.equals(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			if(null != jarFile){
				jarFile.close();
			}
		}
		return myClassName;
	}

	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * @param urls URL集合
	 * @param packagePath 包路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 * @throws Exception
	 */
	private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) throws Exception {
		List<String> myClassName = new ArrayList<String>();
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				URL url = urls[i];
				String urlPath = url.getPath();
				// 不必搜索classes文件夹
				if (urlPath.endsWith("classes/")) {
					continue;
				}
				String jarPath = urlPath + "!/" + packagePath;
				myClassName.addAll(getClassNameByJar(jarPath, childPackage));
			}
		}
		return myClassName;
	}
}