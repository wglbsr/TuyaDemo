# Tuaya Open Api 调用Demo和SDK
# Tuya Open Message 调用Demo和SDK

#打包
mvn package assembly:single

#运行
#-D为需要添加参数时的前缀
#token为其中一个参数，连接凭证，可选
#showContent，是否显示收到的消息内容，默认为0不显示，不为0则为显示，可选
#url，websocket的完整连接（包括token），将会覆盖输入的token，可选
# > log.log显示日志，可选
java -Dtoken=XXXXXXXXXX -Durl=XXXXXXXX -DshowContent=1 -jar youni-smart-home-consumer-jar-with-dependencies.jar > log.log