# News
新闻客户端
算是实现了新闻列表和查看新闻的基本功能,刚开始做这个的时候，学Android也没多长时间，现在想想，这个项目工作量好像不太大。所以后续会添加一些功能，比如天气什么的。


下面放出一些预览图：

![新闻列表1](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/svmj8sKGv6ZyjJ.gIb7Cy4vPUCYC0YbsZDmMgVCUAQs!/m/dA0BAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)
![新闻列表2](http://a4.qpic.cn/psb?/V13Qafg91qhjK9/NeQRtuiA..fb1x*gfDgJJCfHfyVlXRhxuDWbry0asCo!/m/dAsBAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)
![新闻列表3](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/rcvjXKnfpFDJ.TEXSkSIBWr7s5XenpmMkR29*MQGuv8!/m/dA0BAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)
![新闻列表4](http://a1.qpic.cn/psb?/V13Qafg91qhjK9/cjZjpY5SsAxq5ZrBwpYGeeeDvAP*SP*VAFCN.4CO3ts!/m/dAwBAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)

![新闻详情](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/bYnaWQMXpCD2*mVFaEywfLVIRUM1sv4XY4Ho.iEui5A!/m/dA0BAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)

#天气
![天气主界面](http://a3.qpic.cn/psb?/V13Qafg91qhjK9/*Hu2pYkzLY4OiI3aXsmCbtUjOjHZOfUQBtNoCvGa2.E!/m/dHIBAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)
![手动选择地点1](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/4usveALVGq6ltNrrETekB4p*aLpbDnN15xjMimEm92A!/m/dOUAAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)
![手动选择地点2](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/bHJRhWSuw9eZHz8kky*gOSI.oubrGk0xzQKCp5xx07U!/m/dI0BAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)
![手动选择地点3](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/tffJPh8JKsuu4lUcOEa*wcHrn*5b8Iqjr1jdlQ9HhHg!/m/dOUAAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-3-3&rf=0-0)

#侧滑
![侧滑](http://a4.qpic.cn/psb?/V13Qafg91qhjK9/ImBe5ICaw0oOUhFnxEm.GwRR0W1SQ1WDHYVHBCREyMM!/c/dAMBAAAAAAAA&ek=1&kp=1&pt=0&bo=gAJyBDgEgAcFCGQ!&sce=0-12-12&rf=0-18)

#图片
![图片](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/arRqhJRSgwP55TQZPA8A4vFD28qS2hnvVCjPOv8l2R4!/c/dHEBAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-2-2&rf=0-0)

![图片2](http://a2.qpic.cn/psb?/V13Qafg91qhjK9/xyhtwuVkNja86sz0HBSnzH3UFvUgqVjuKIDJ.26kwRc!/c/dHEBAAAAAAAA&ek=1&kp=1&pt=0&bo=7gI3BQAAAAAFAP0!&sce=60-2-2&rf=0-0)

至此大部分基础功能都已经完成了，天气还有一个定位待完成。后续应该还会更新几个东西。不过这个东西是在初学的时候做的，所以很多东西用的还是基础的，比如Http请求用的是HttpUrlConnection。不过因为第一个学会用的就是这个，所以用起来还是很有亲切感的。好了，感慨就到这了，后续我可能会重构这个项目，用一些更高大上的东西，不过那可能是另外一个仓库的事情了。

这几天优化了一下Fragment的使用，比如不像以前每次都new Fragment了，采用add,hide,show的方式展示Fragment。主要的新闻列表，天气和图片都做了缓存，在离线情况下也有东西能看了。添加了一个服务监听是否从网络断开，总之完善了不少细节。不过也还有一些问题。

以前一直要说的完善新闻详情简单的设置了一下……然后天气定位……最近一直处于很难联网的状态，搞这些心有余而力不足。最后在侧滑添加了一个侧滑，算是先挖一个坑吧。天气界面算是丑的……其实数据是比较详细的，但是当时比较懒，只想做个简单的天气功能，这也是个坑，至于填不填看以后的具体情况了。如果能完善这三个东西，可能就没有后续的更新了吧，毕竟是从初学的时候开始尝试做的东西，做到现在也差不多了。

#关于我
我的简书：http://www.jianshu.com/users/e1fed0fb341b/timeline

我的邮箱：xiasuhuei321@163.com

#注意
本项目中所使用的api是从别的开源项目中看到的，仅供学习之用！如果上架应用想要使用这些api请联系api所有者以争取同意！
