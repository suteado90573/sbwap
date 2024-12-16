# sbwap
SpringBoot Web Application

---
# 子プロジェクトの配置方法

```
[settings.gradle]

include('sblib')

rootProject.name = 'sbwap'
rootProject.children.each { it.name = rootProject.name + '_' + it.name }
```

- 「settings.gradle」に、記述を追加する。

  - 「include('[取り込む子プロジェクトのフォルダ]')」を記述する。
  - 「rootProject～」により、取り込む子プロジェクトに親プロジェクト名を付与。  
    これにより、複数の親プロジェクトで同じ子プロジェクトを取り込んでも、  
    バッティングしないプロジェクト名とできる。(eclipseなどでも取り込める  
    ようにするために必要な設定)
  
```
[build.gradle]

// ライブラリプロジェクトを使用するための設定
implementation project(':sbwap_sblib')
```

- 「build.gradle」に、記述を追加する。

  - このとき記述するのは「settings.gradle」で設定した親プロジェクト名を  
    プレフィックスとする、バッティングしないプロジェクト名とすること。

---
# プロパティファイルの記述内容について

- 「application.properties」にはSpringBootの起動時に読み込む設定を記述する。  
  (デフォルトの挙動を変えたいときに、SpringBootが持っているプロパティの設定を  
  個別に指定するための設定ファイル)
- ユーザが独自に定義したプロパティを認識させたい場合は「custom.properties」  
  のような別のプロパティファイルとすること。
- ユーザ定義プロパティファイル(ここでは例示として「custom.properties」という  
  ファイル名とする)を取り込むためには、「application.properties」に次のような  
  設定を追加する。

```
[application.properties]

## Custom property settings
spring.config.import=custom.properties
```

---
# プロパティファイルの配置について

- 「application.properties」は、親となるWebアプリ側にのみ配置する。  
  (　「custom.properties」のような、従属するプロパティファイルも同様)
- ただし従属するプロジェクト側を単独のGradleプロジェクトとし、ビルドに  
  よって全テストを行う場合、「application.properties」及び従属する  
  「custom.properties」などのプロパティファイルは、test/resources配下  
  に配置する。(こうすると、JUnit全テスト時にはtest/resources配下に配置  
  したプロパティファイルに基づいて動作させることができる)

---
# 【ToDo】環境ごとのプロパティファイルについて

- 【ToDo】サンプル及び記載を追加する。

---
# 資材配置について

- 子プロジェクトに集めて共通化したい資材は、子プロジェクト側に配置する。  
  ＜例＞共通メッセージプロパティ、共通クラス群(サービス層以下全部)  
- 親プロジェクト側にはプロジェクト固有の資材のみを配置する。  
  ＜例＞コントローラ、親クラスの画面資材

---
# Gradleビルドが出る場合の対応について
Gradleビルド時にテストエラーとなってしまう場合の対応は、次の通り。
- テストエラーを確認する。
- インジェクションするクラスが存在しないというエラーの場合、  
  使っているSpringBootの機能に必要な設定が「application.properties」  
  に記載されているか確認する。  
  ＜例＞  
  - データベースアクセスがある場合、「application.properties」に  
    「spring.datasource～」で始まる諸設定がないと動かない。
  - JavaMailSenderを使う場合、「spring.mail.host」と「spring.mail.port」  
    の設定がないと動かない。

---
# h2データベースを使用するための設定について
「build.gradle」にh2を使用するための設定記述が必要。
```
[build.gradle]

	// h2DBを使用するための設定
	runtimeOnly 'com.h2database:h2'
```

「application.properties」に、データソースの設定が必要。

```
[application.properties]

## datasource settings
spring.datasource.url=jdbc:h2:~/test
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:dbinit/schema.sql 
spring.sql.init.data-locations=classpath:dbinit/data.sql 
```
- 「spring.sql.init.mode=always」を指定すると、SpringBoot起動時に  
  必ず毎回初期化SQLを実行してオンメモリのDBスキーマを作成する。  
  (簡易動作確認に便利なやり方)
- 「spring.sql.init.schema-locations」でDB構築SQL、  
  「spring.sql.init.data-locations」でDB初期化SQLを指定できる。  
  前者はDROP-CREATEのSQLを記述する。(SpringBoot起動時、毎回DB初期化する)  
  後者は初期データ投入のSQLを記述する。

また「application.properties」に次の設定を追加することで、  
ブラウザからh2のコンソールにアクセスできるようになる。

```
[application.properties]

## h2 settings
spring.h2.console.enabled=true
```
SpringBootを起動すると、コンソールに次のような表示が出る。
```
[SpringBoot起動時のコンソール表示]

[ログ時刻やクラス名] H2 console available at '/h2-console'. Database available at 'jdbc:h2:~/test'
```
- ブラウザを開き、次のURLにアクセスする。  
  http://localhost:8080/h2-console
- 「JDBC URL」の項に上記コンソールに表示されている「jdbc:h2:~/test」の部分を入力する。
- 「Connect」(日本語表示なら「接続」　)をクリックし、ログインできればアクセスに成功している。  
  SELECTを実行すると、投入されている初期データを確認できる。

---
# SpringDocの使用方法
「build.gradle」にSpringDocを使用するための設定記述が必要。
```
[build.gradle]

	// SpringDocを使用するための設定
	implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0'
```

「application.properties」に、データソースの設定が必要。

```
[application.properties]

## SpringDoc
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```
- ブラウザを開き、次のURLにアクセスすると、自動生成されたAPI仕様書が確認できる。  
http://localhost:8080/swagger-ui.html
- 次のURLにアクセスすると、swaggerのyamlをダウンロードできる。  
http://localhost:8080/api-docs.yaml

---
# 【ToDo】MyBatisでスネーク記法のDBカラム名をキャメル記法のエンティティに対応させる方法

- 【ToDo】サンプルはあるので、必要な設定を記述。

---
# 【ToDo】MyBatisでSQLのログを表示する方法

- 【ToDo】サンプルはあるので、必要な設定を記述。

---
# 【ToDo】メッセージプロパティの設定方法

- 【ToDo】サンプルはあるので、必要な設定を記述。

---
# 【ToDo】メール送信方法

- 【ToDo】サンプルはあるので、必要な設定を記述。

---
# 【ToDo】redisによるセッション情報の共有

- 【ToDo】サンプル及び記載を追加する。

---
# 【ToDo】パッケージ構成について

- 【ToDo】まとまりとして別で記述したい。  
  上記のうち細かいノウハウは、カテゴリごとにナレッジとして整理する。
  パッケージ構成やプロパティなどについては、ナレッジではなく基本事項として
  フォルダ分けして整理したい。

---
# 【ToDo】コントローラ、サービス、ユーティリティ、エンティティ、マッパーなど

- 【ToDo】まとまりとして別で記述したい。  

---
# 【ToDo】ビルド手順

- 【ToDo】まとまりとして別で記述したい。  

---
# 【ToDo】JUnit

- 【ToDo】まとまりとして別で記述したい。  
  モックについて記述。
