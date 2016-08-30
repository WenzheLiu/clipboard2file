我们经常在手机上看文章，时间久了眼睛会很累。不如用耳朵听。有些App会发音，可以把文字朗读出来，比如多看阅读就是这类软件中比较好的一个。但是它不能读剪贴板的文字，我一般是这样用的：

1. 有段想听的文字，选中，复制。
2. 打开文件浏览器（如ES），创建一个txt文件，编辑，粘贴那段想听的文字，保存文件。
3. 用多看阅读打开那个文件，然后朗读。

这个步骤很繁琐，为了简化流程，我最近写了一个Android上的小工具，Clipboard to file。这个工具是在Android手机上将剪贴板保存为文件的快捷APP，同时提供按钮方便打开，从而让用户无需关注文件的保存路径（目录在：/sdcard/clipboard2file/年/月/日/，当然用户一般不用知道，存储时会有tip告知用户）

![这里写图片描述](http://img.blog.csdn.net/20160829223527543)

有了它，流程变得很简单：

1. 有段想听的文字，选中，复制。
2. 打开Clipboard to file，点击粘贴按钮（标题和内容左边各有一个粘贴按钮，都点一下，剪贴板第一行自动设置为标题）
3. 点击Clipboard to file的打开按钮，打开方式选多看阅读就可以朗读了。

下面的视频演示了这个过程：

![这里写图片描述](http://img.blog.csdn.net/20160828143454803)

作为软件工程师，平时读的文章很多会带有一长串代码，把代码读出来会很长又难听。能不能忽略这些代码呢？

这个app有个按钮（图标为a，在save按钮上面），点击它会变成国旗的图标，这时点击保存或者打开，代码就不会出现在多看阅读中了。（不包含中文的行将会被过滤出去）

—— 本博客所有内容均为原创，转载请注明作者和出处 ——–

作者：刘文哲

联系方式：liuwenzhe2008@qq.com

博客：http://blog.csdn.net/liuwenzhe2008

源码： https://github.com/WenzheLiu/clipboard2file

APP下载：https://github.com/WenzheLiu/clipboard2file/blob/master/app/clipboard2file.apk
