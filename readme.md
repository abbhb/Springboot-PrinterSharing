# AI综合服务平台



### Version：1.8.0

## 环境准备
- rabbitmq 最新版 加上延迟队列插件 推荐docker安装 单体文件太多
- redis 最新版即可
- mysql8.0.20
- minio 最新版即可


- [AI服务平台主后端](https://github.com/abbhb/Springboot-PrinterSharing)
- [AI服务平台前端(本项目也是其中一部分的后端)](https://github.com/abbhb/Vue-PrinterSharing)
- [AI服务平台DOC后端](https://github.com/abbhb/Printer-Doc)


## 运行
直接maven拉一下依赖
运行即可
```text
注意:如果是java8以上版本需要加上VM参数,不然可能出现报错
一些反射找不到(但我现在没加也没报错)
```
## 注意:
+ swagger在上线前最好关掉 在主函数上注释掉注解即可
+ minio 报连接错误可能是时区问题，minio需要美国1时区
+ token 存在cookies 方便管理
+ 运行前需要将根目录下的libs的jar包导入项目的resources,将dll导入jre/bin目录,高版本java放在jdk/bin下方