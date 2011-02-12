package_hr:
"%JAVA_HOME%\bin\native2ascii.exe" -encoding UTF8 "D:\Java workspaces\Projekti\CChat\src\hr\chus\cchat\struts2\action\package_hr-WORK.properties" "D:\Java workspaces\Projekti\CChat\src\hr\chus\cchat\struts2\action\package_hr.properties"


MYSQL


--- CREATE DB ---

CREATE DATABASE cchat DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER cchat_user@localhost IDENTIFIED BY 'a54Oa#22?3fsF';
GRANT ALL PRIVILEGES ON cchat.* TO cchat_user@127.0.0.1 WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON cchat.* TO cchat_user@localhost WITH GRANT OPTION;
FLUSH PRIVILEGES;


Test SMS url: http://localhost:8080/CChat/ReceiveSms?msisdn=12&sc=66111&serviceProviderName=VIP&text=TestSMS