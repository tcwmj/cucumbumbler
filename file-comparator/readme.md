1、配置java环境
   1.1
	进入dos命令行，输入 "java -version",如果出现如下字样，说明您本地有java环境，直接进入步骤2.
	   java version "1.6.0_16"
	   Java(TM) SE Runtime Environment (build 1.6.0_16-b01)
	   Java HotSpot(TM) Client VM (build 14.2-b01, mixed mode, sharing)

   1.2
	如果没有安装java，请按照如下步骤操作：
	a.打开电脑T盘，进入jack chen 目录下，将文件夹 java 整个拷贝到本地，例如放到D:\根目录
	
	b.打开我的电脑--属性--高级--环境变量 

	c.新建系统变量JAVA_HOME 和CLASSPATH 
	变量名：JAVA_HOME 
	变量值：d:\Java\jdk1.6.0_16          (放到D盘是因为前面把java文件夹放到D盘了)
	变量名：CLASSPATH 
	变量值：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;

	d. 选择“系统变量”中变量名为“Path”的环境变量，双击该变量，把JDK安装路径中bin目录的绝对路径，添加到Path变量的值中，并使用半角的分号和已有的路径进行分隔。 
	变量名：Path 
	变量值：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
	
	e. 再按照1.1步骤操作，应该已经配置成功
2:配置Maven
	2.1 : 打开电脑T盘，进入Jack chen目录下，将apache-maven-3.1.0 文件夹拷贝至本地电脑，例如D盘
	2.2 ：打开我的电脑--属性--高级--环境变量 ，在 “系统变量”中变量名为“Path”的环境变量后面把maven的目录追加进去,例如 ;D:\apache-maven-3.1.0
	2.3 ：新开一个Dos窗口，输入 mvn -version，出现如下字样说明配置成功：
		Apache Maven 3.1.0 (xxxxxxxxxx.......)

3:修改ResultConfig.xml文件夹，该文件夹将保存批量对比生成的html的结果
	<resultDir>D:\a\b\c\</resultDir>
	例如该配置，生成的对比结果将放在 D:\a\b\c\目录下
	请注意：文件夹最后请用斜杠 \ 结尾
	
	<StatisticsFile>D:\StatisticsReport.xls</StatisticsFile>
	case执行统计结果会存储到该文件里面，便于和QC同步

4：修改TestConfig.xml文件夹，该文件将配置执行哪些模块Report对比
	<Suite>
		<!-- golden file文件夹所在路径 -->
		<parameter name="goldenDir" value="D:/svndoc/trunk/Data/Reports/GoldenData/" />

		<!-- result file 文件夹所在路径 -->
		<parameter name="resultDir" value="D:/svndoc/trunk/Data/Reports/ResultData/" />

		<test name="AHV">
			<!-- 需要对比的文件夹名字-->
			<parameter name="folderName" value="AHV" />
			
			<!--想要忽略的列，多列请用竖线 | 分割开来，忽略的列将不参与对比-->
			<parameter name="excludeColumns" value="FX Rate" />
			
			<!--想定义为Key的列，多列的话请用竖线 | 分割开来-->
			<parameter name="keyColumns" value="" />
			
			<!--用以执行对比的Class，请不要做任何修改-->
			<classes>
				<class name="com.woxiangbo.projects.mycompare.SimpleTest" />
			</classes>
		</test>
	</Suite>
	每一个<test>...</test>表示一个执行模块，如果只想执行某个特定的模块，可以把其它的 <test>...</test>注释掉。
	注释请用 XML 注释语法 <!--开头,-->结尾。

5：打开Dos窗口，进入到该工具所在目录，例如 D:\ReportBATCompare
	输入命令 ：mvn clean test
	执行完毕，可以在当前目录的 target目录下，找到surefire-reports文件夹，找到 index.html,即可查看到所有对比结果
	这里对比结果链接的是在步骤3中的文件，只是看起来更规范，更整齐。也可以直接打开步骤3所配置的文件夹路径，一个一个查看

	NOTICE：在对比结果页面，如果有任何的error信息，则标示对比结果不可信，请根据error信息提示后，重新执行命令并对比
