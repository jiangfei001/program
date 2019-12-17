function adSlider(t){adimg[t]={},adimg[t].nowFrame=0,adimg[t].oTransImg=$("#trans"+t+"Img");var e=adimg[t].oTransImg;if(e){var s=$("#trans"+t+"Obj");e.css("display","none"),s.css("left",e.css("left")),s.css("top",e.css("top")),s.css("width",e.css("width")),s.css("height",e.css("height")),s.css("zIndex",e.css("zIndex"));for(var i=s.get(0).getElementsByTagName("img"),a=0;a<i.length;a++)i[a].style.width=e.css("width"),i[a].style.height=e.css("height"),i[a].style.display="block";s.aviaSlider({blockSize:{height:parseInt(e.css("height").replace("px",""))/4,width:parseInt(e.css("width").replace("px",""))/8},transition:"slide",display:"all",transitionOrder:["diagonaltop","diagonalbottom","topleft","bottomright","random"]})}}function runAdImg(t,e){var s=document.getElementById("trans"+t+"Img"),i=document.getElementById("trans"+t+"Obj");s&&i&&(adimg[t]=new GalleryComponent(i))}function componentGesture(t,e){var s=t.target.parentNode;s.className.search("polaroid")>-1&&(s=t.target.parentNode.parentNode);var i=e;switch(t.type){case"touch":last_scale=scale,last_rotation=rotation,lastWidth=i.offsetWidth,lastHeight=i.offsetHeight,lastPosX=posX=i.offsetLeft,lastPosY=posY=i.offsetTop;break;case"drag":posX=t.gesture.deltaX+lastPosX,posY=t.gesture.deltaY+lastPosY;break;case"transform":rotation=last_rotation+t.gesture.rotation,scale=Math.max(1,Math.min(last_scale*t.gesture.scale,10));break;case"dragend":lastPosX=posX,lastPosY=posY}i.style.left=posX+"px",i.style.top=posY+"px",i.style.width=Math.ceil(lastWidth*t.gesture.scale)+"px",i.style.height=Math.ceil(lastHeight*t.gesture.scale)+"px"}function adimgGesture(t,e){var s;if(e.id&&(s=e.id.replace("trans","").replace("Obj","")),s&&adimg[s]){var i=adimg[s];switch(t.type){case"touch":last_scale=scale,last_rotation=rotation,lastWidth=i.currentFrame.get(0).offsetWidth,lastHeight=i.currentFrame.get(0).offsetHeight,lastPosX=posX=i.currentFrame.get(0).offsetLeft,lastPosY=posY=i.currentFrame.get(0).offsetTop,i.dragTimeout=setTimeout(function(){i.canDrag=!0},200);break;case"release":i.canDrag=!1;break;case"drag":posX=t.gesture.deltaX+lastPosX,posY=t.gesture.deltaY+lastPosY,i.canDrag&&i.position(posX+"px",posY+"px");break;case"transform":rotation=last_rotation+t.gesture.rotation,scale=Math.max(1,Math.min(last_scale*t.gesture.scale,10));var a=Math.ceil(lastWidth*t.gesture.scale);a>=e.offsetWidth&&a<=2*e.offsetWidth&&i.resize(Math.ceil(lastWidth*t.gesture.scale)+"px",Math.ceil(lastHeight*t.gesture.scale)+"px");break;case"dragend":lastPosX=posX,lastPosY=posY;break;case"swipe":clearTimeout(i.dragTimeout);break;case"swipeleft":i.switchNext();break;case"swiperight":i.switchPrevious()}}}function weatherGesture(t,e){var s=t.target.parentNode;s.className.search("polaroid")>-1&&(s=t.target.parentNode.parentNode);var i=e;switch(t.type){case"touch":last_scale=scale,last_rotation=rotation,lastWidth=i.offsetWidth,lastHeight=i.offsetHeight,lastPosX=posX=i.offsetLeft,lastPosY=posY=i.offsetTop;break;case"drag":posX=t.gesture.deltaX+lastPosX,posY=t.gesture.deltaY+lastPosY;break;case"transform":rotation=last_rotation+t.gesture.rotation,scale=Math.max(1,Math.min(last_scale*t.gesture.scale,10));var a=Math.ceil(lastWidth*t.gesture.scale),r=Math.ceil(lastHeight*t.gesture.scale);$(e).find("*").css("width",a+"px").css("height",r+"px"),$(e).find("img").css("width",a/2+"px").css("height",a/2+"px").parent().css("width",a/2+"px").css("height",a/2+"px"),$(e).find("div[id=weatherTable]").css("font-size",a/10+"px");break;case"dragend":lastPosX=posX,lastPosY=posY}i.style.left=posX+"px",i.style.top=posY+"px",i.style.width=Math.ceil(lastWidth*t.gesture.scale)+"px",i.style.height=Math.ceil(lastHeight*t.gesture.scale)+"px"}function charGesture(t,e){var s=t.target.parentNode;s.className.search("polaroid")>-1&&(s=t.target.parentNode.parentNode);var i=e;switch(t.type){case"touch":last_scale=scale,last_rotation=rotation,lastWidth=i.offsetWidth,lastHeight=i.offsetHeight,lastPosX=posX=i.offsetLeft,lastPosY=posY=i.offsetTop;break;case"drag":posX=t.gesture.deltaX+lastPosX,posY=t.gesture.deltaY+lastPosY;break;case"transform":rotation=last_rotation+t.gesture.rotation,scale=Math.max(1,Math.min(last_scale*t.gesture.scale,10));var a=Math.ceil(lastWidth*t.gesture.scale),r=Math.ceil(lastHeight*t.gesture.scale);$(e).find("*").css("width",a+"px").css("height",r+"px").css("font-size",parseInt(a/10)+"px"),$(e).css("font-size",parseInt(a/10)+"px");break;case"dragend":lastPosX=posX,lastPosY=posY}i.style.left=posX+"px",i.style.top=posY+"px",i.style.width=Math.ceil(lastWidth*t.gesture.scale)+"px",i.style.height=Math.ceil(lastHeight*t.gesture.scale)+"px"}function marqueeGesture(t,e){var s=t.target.parentNode;s.className.search("polaroid")>-1&&(s=t.target.parentNode.parentNode);var i=e;switch(t.type){case"touch":last_scale=scale,last_rotation=rotation,lastWidth=i.offsetWidth,lastHeight=i.offsetHeight,lastPosX=posX=i.offsetLeft,lastPosY=posY=i.offsetTop;break;case"drag":posX=t.gesture.deltaX+lastPosX,posY=t.gesture.deltaY+lastPosY;break;case"transform":rotation=last_rotation+t.gesture.rotation,scale=Math.max(1,Math.min(last_scale*t.gesture.scale,10));var a=Math.ceil(lastWidth*t.gesture.scale);Math.ceil(lastHeight*t.gesture.scale);$(e).find("*").css("width",a+"px").css("height",parseInt(a/10)+"px").css("font-size",parseInt(a/15)+"px");break;case"dragend":lastPosX=posX,lastPosY=posY}i.style.left=posX+"px",i.style.top=posY+"px",i.style.width=Math.ceil(lastWidth*t.gesture.scale)+"px",i.style.height=Math.ceil(lastHeight*t.gesture.scale)+"px"}function videoGesture(t,e){var s=t.target.parentNode;s.className.search("polaroid")>-1&&(s=t.target.parentNode.parentNode);var i=e;switch(t.type){case"touch":last_scale=scale,last_rotation=rotation,lastWidth=i.offsetWidth,lastHeight=i.offsetHeight,lastPosX=posX=i.offsetLeft,lastPosY=posY=i.offsetTop;break;case"drag":posX=t.gesture.deltaX+lastPosX,posY=t.gesture.deltaY+lastPosY;break;case"dragend":lastPosX=posX,lastPosY=posY}i.style.left=posX+"px",i.style.top=posY+"px",i.style.width=Math.ceil(lastWidth*t.gesture.scale)+"px",i.style.height=Math.ceil(lastHeight*t.gesture.scale)+"px"}function GetElementPosition(t){var e=getElementPosition(t);return{eleWidth:e.width+"px",eleHeight:e.height+"px",eleLeft:e.left+"px",eleTop:e.top+"px"}}function getElementPosition(t,e){var s=function(t,e){var i=[t.offsetLeft,t.offsetTop],a=i[e];return null!=t.offsetParent&&(a+=s(t.offsetParent,e)),a},i=function(t,e){var s=0;if(t!=window.top){s=[t.frameElement.offsetLeft,t.frameElement.offsetTop][e],t.parent!=window.top&&(s+=i(t.parent,e))}return s},a=function(t,a){var r=s(t,a);return e&&(r+=i(window.self,a)),r},r=t.style.display;"none"==r&&(t.style.display="");var o={width:t.offsetWidth,height:t.offsetHeight,left:a(t,0),top:a(t,1)};return r!=t.style.display&&(t.style.display=r),o}function allPlayerStop(t,e,s,i){if("undefined"!=typeof android&&($("div[hardwareAcceleration='true']",window.self.document).each(function(t,e){var s=$(e).css("z-index");navigator.Marquee.stop(s),navigator.Marquee.stop(this.id)}),$("marquee[hardwareAcceleration='true']",window.self.document).each(function(t,e){var s=$(e).css("z-index");navigator.Marquee.stop(s),navigator.Marquee.stop(this.id)}),null!=document.getElementById("pdfImg")&&android.PDFStop(),null!=document.getElementById("livevideoImg")&&android.StreamStop(),null!=document.getElementById("audioVideoImg")&&android.AVStop(),null!=document.getElementById("videoWplImg")&&android.VideoStop()),t){if("undefined"!=typeof android&&android.LoadingDialog(),e&&(e=window.top.document.getElementsByName(e)[0]),("checked"==s||1==s)&&!isNaN(i)&&parseInt(i)>1e3){var a=location.href;e&&e.src&&(a=e.src),t=this.setQueryString("redirect_url",this.deleteQueryString("redirect_time",this.deleteQueryString("redirect_url",a)),t),t=this.setQueryString("redirect_time",i,t)}else this.getQueryString("redirect_url")&&(t=this.setQueryString("redirect_url",this.getQueryString("redirect_url"),t),t=this.setQueryString("redirect_time",this.getQueryString("redirect_time"),t));e?e.src=t:window.top.location.href=t}}function getQueryString(t,e){return e||(e=location.href),new RegExp("(^|\\?|&)"+t+"=([^&]*)(\\s|&|$)","i").test(e)?unescape(RegExp.$2.replace(/\+/g," ")):""}function setQueryString(t,e,s){s||(s=location.href);var i=new RegExp(t+"=([^&]*)(\\s||$)","i");return i.test(s)?s.replace(i,t+"="+e):s.indexOf("?")<0||""==s.substring(s.indexOf("?"),s.length-1)?s+(s.indexOf("?")<0?"?":"")+t+"="+e:""!=s.substring(s.indexOf("?"),s.length-1)?s+"&"+t+"="+e:void 0}function deleteQueryString(t,e){e||(e=location.href);var s=new RegExp(t+"=([^&]*)(\\s|&|$)","i");return e=e.replace(s,""),e.lastIndexOf("&")==e.length-1&&(e=e.substring(0,e.length-1)),e.lastIndexOf("?")==e.length-1&&(e=e.substring(0,e.length-1)),e}document.documentElement.className+="js_active",function(t){t.fn.aviaSlider=function(e){var s={slides:"li",animationSpeed:900,autorotation:!0,autorotationSpeed:3,appendControlls:"",slideControlls:"items",blockSize:{height:"full",width:"full"},betweenBlockDelay:60,display:"topleft",switchMovement:!1,showText:!0,transition:"fade",backgroundOpacity:.8,transitionOrder:["diagonaltop","diagonalbottom","topleft","bottomright","random"]},i=t.extend(s,e);return this.each(function(){var e=t(this),s=e.find(i.slides),a=s.find("img"),r=s.length,o=s.width(),n=s.height(),l=0,c=0,h=!1,d=0,p="active_item",f="",u=!0,g="",m="",y="",v=[];"full"==i.blockSize.height&&(i.blockSize.height=n),"full"==i.blockSize.width&&(i.blockSize.width=o),e.methods={init:function(){var a=0,c=0,h=!0,d="";for(s.filter(":first").css({"z-index":"5",display:"block"});h;)l++,d="-"+a+"px -"+c+"px",t('<div class="kBlock"></div>').appendTo(e).css({zIndex:20,position:"absolute",display:"none",left:a,top:c,height:i.blockSize.height,width:i.blockSize.width,backgroundPosition:d}),a+=i.blockSize.width,a>=o&&(a=0,c+=i.blockSize.height),c>=n&&(h=!1);m=e.find(".kBlock"),v.topleft=m,v.bottomright=t(m.get().reverse()),v.diagonaltop=e.methods.kcubit(m),v.diagonalbottom=e.methods.kcubit(v.bottomright),v.random=e.methods.fyrandomize(m),s.each(function(){t.data(this,"data",{img:t(this).find("img").attr("src")})}),r<=1?e.aviaSlider_preloadhelper({delay:200}):(e.aviaSlider_preloadhelper({callback:e.methods.preloadingDone}),e.methods.appendControlls().addDescription())},appendControlls:function(){if("items"==i.slideControlls){var a=i.appendControlls||e[0];f=t("<div></div>").addClass("slidecontrolls").insertAfter(a),s.each(function(s){t('<a href="#" class="ie6fix '+p+'"></a>').appendTo(f).bind("click",{currentSlideNumber:s},e.methods.switchSlide),p=""}),f.width(f.width()).css("float","none")}return this},addDescription:function(){i.showText&&s.each(function(){var e=t(this),s=e.find("img").attr("alt"),a=s.split("::");""!=a[0]&&(s=void 0!=a[1]?"<strong>"+a[0]+"</strong>"+a[1]:a[0]),""!=s&&t("<div></div>").addClass("feature_excerpt").html(s).css({display:"block",opacity:i.backgroundOpacity}).appendTo(e.find("a"))})},preloadingDone:function(){u=!1,s.css({backgroundColor:"transparent",backgroundImage:"none"}),i.autorotation&&(e.methods.autorotate(),a.bind("click",function(){clearInterval(g)}))},autorotate:function(){g=setInterval(function(){c++,c==r&&(c=0),e.methods.switchSlide()},1e3*parseInt(i.autorotationSpeed)+i.betweenBlockDelay*l+i.animationSpeed)},switchSlide:function(a){var r=!1;if(void 0==a||u||(c!=a.data.currentSlideNumber?c=a.data.currentSlideNumber:r=!0),void 0!=a&&clearInterval(g),!u&&0==r){u=!0;var o=s.filter(":visible"),n=s.filter(":eq("+c+")"),p=t.data(n[0],"data").img,w="url("+p+")";i.slideControlls&&(f.find(".active_item").removeClass("active_item"),f.find("a:eq("+c+")").addClass("active_item")),y=v[i.display],s.find(">a>img").css({opacity:1,visibility:"visible"}),!i.switchMovement||"topleft"!=i.display&&"diagonaltop"!=i.display||(0==h?(y=v[i.display],h=!0):("topleft"==i.display&&(y=v.bottomright),"diagonaltop"==i.display&&(y=v.diagonalbottom),h=!1)),"random"==i.display&&(y=e.methods.fyrandomize(m)),"all"==i.display&&(y=v[i.transitionOrder[d]],++d>=i.transitionOrder.length&&(d=0)),y.css({backgroundImage:w}).each(function(s){var a=t(this);setTimeout(function(){var t=new Array;"drop"==i.transition?(t.css={height:1,width:i.blockSize.width,display:"block",opacity:0},t.anim={height:i.blockSize.height,width:i.blockSize.width,opacity:1}):"fade"==i.transition?(t.css={display:"block",opacity:0},t.anim={opacity:1}):(t.css={height:1,width:1,display:"block",opacity:0},t.anim={height:i.blockSize.height,width:i.blockSize.width,opacity:1}),a.css(t.css).animate(t.anim,i.animationSpeed,function(){s+1==l&&e.methods.changeImage(o,n)})},s*i.betweenBlockDelay)})}return!1},changeImage:function(t,e){t.css({zIndex:0,display:"none"}),e.css({zIndex:3,display:"block"}),y.fadeOut(1*i.animationSpeed/3,function(){u=!1})},fyrandomize:function(e){var s=e.length,i=t(e);if(0==s)return!1;for(;--s;){var a=Math.floor(Math.random()*(s+1)),r=i[s],o=i[a];i[s]=o,i[a]=r}return i},kcubit:function(e){var s=e.length,a=t(e),r=0,c=Math.ceil(n/i.blockSize.height),h=Math.ceil(o/i.blockSize.width),d=l/h,p=l/c,f=0,u=0,g=0,m=0,y=!1,v=!1;if(0==s)return!1;for(g=0;g<s;g++)a[g]=e[r],r%p==0&&l-g>p||(u+1)%d==0?(r-=(p-1)*u-1,u=0,f++,v=!1,m>0&&(u=m,r+=(p-1)*u)):(r+=p-1,u++),(f%(p-1)==0&&0!=f&&0==m||1==y&&0==v)&&(f=.1,m++,y=!0,v=!0);return a}},e.methods.init()})}}(jQuery),function(t){t.fn.aviaSlider_preloadhelper=function(e){var s={fadeInSpeed:800,delay:0,callback:""},i=t.extend(s,e);return this.each(function(){var e=jQuery(this),s=e.find("img").css({opacity:0,visibility:"hidden",display:"block"}),a=s.length;e.operations={preload:function(){return s.each(function(s,i){var a=new Image,r=t(this);a.src=this.src,a.complete?e.operations.showImage(r):t(a).bind("error load",{currentImage:r},e.operations.showImage)}),this},showImage:function(e){a--,void 0!=e.data.currentImage&&(e=e.data.currentImage),i.delay<=0&&e.css("visibility","visible").animate({opacity:1},i.fadeInSpeed),0==a&&(i.delay>0?(s.each(function(e,s){var a=t(this);setTimeout(function(){a.css("visibility","visible").animate({opacity:1},i.fadeInSpeed,function(){t(this).parent().removeClass("preloading")})},i.delay*(e+1))}),""!=i.callback&&setTimeout(i.callback,i.delay*s.length)):""!=i.callback&&i.callback())}},e.operations.preload()})}}(jQuery);var adimg=new Array,GalleryComponent=function(){this.init.apply(this,arguments)};GalleryComponent.prototype={id:"",name:"Gallery",init:function(t,e){if(this.id=t.id.replace("trans","").replace("Obj",""),this.container=$(t),this.placeholder=$("#"+t.id.replace("Obj","Img")),this.placeholder.css("display","none"),this.container.css("left",this.placeholder.css("left")),this.container.css("top",this.placeholder.css("top")),this.container.css("width",this.placeholder.css("width")),this.container.css("height",this.placeholder.css("height")),this.container.css("zIndex",this.placeholder.css("zIndex")),this.effectTime=this.placeholder.attr("EffectTime"),this.effectTime=this.effectTime&&!isNaN(this.effectTime)?this.effectTime:0,this.frames=this.container.get(0).getElementsByTagName("li"),0==this.frames.length&&(this.frames=this.container.get(0).getElementsByTagName("img")),1==this.frames.length)return void this.placeholder.css("display","");for(var s=0;s<this.frames.length;s++)this.frames[s].style.width=this.placeholder.css("width"),this.frames[s].style.height=this.placeholder.css("height"),this.frames[s].style.overflow="hidden",this.frames[s].style.position="relative";this.max=this.frames.length-1,this.index=this.max,this.startAutoSwitch()},switchGallery:function(t,e){if(t<0?t=0:t>this.max&&(t=this.max),this.index=t,this.currentFrame=$(this.frames[this.index]),this.currentFrame.css("width",this.placeholder.css("width")),this.currentFrame.css("height",this.placeholder.css("height")),this.currentFrame.css("left",0),this.currentFrame.css("top",0),$(this.frames[0]).css("z-index",0==this.index?"999":""),0<e<this.max&&(this.previous=e,this.previousFrame=$(this.frames[this.previous]),this.frames[this.previous].style.marginBottom="-"+this.frames[this.previous].style.height),This=this,this.currentFrame.get(0).filters,1)this.currentFrame.fadeIn(1e3*this.effectTime,function(){This.previousFrame&&This.previousFrame.hide(),This.previousFrame=This.currentFrame});else{var s=this.currentFrame.attr("effectType");switch(s){case"23":this.currentFrame.get(0).style.filter="RevealTrans(duration = "+this.effectTime+",transition="+parseInt(22*Math.random())+");";break;case"24":this.currentFrame.get(0).style.filter="progid:DXImageTransform.Microsoft.Wipe(GradientSize=1.0,wipeStyle=0, motion=forward)";break;default:this.currentFrame.get(0).style.filter="RevealTrans(duration = "+this.effectTime+",transition="+s+");"}this.currentFrame.get(0).filters[0].Apply(),this.currentFrame.show(),"25"!=s?(this.currentFrame.get(0).filters[0].Play(this.effectTime),setTimeout(function(){This.previousFrame&&This.previousFrame.hide(),This.previousFrame=This.currentFrame},1e3*this.effectTime)):this.currentFrame.get(0).filters[0].Stop()}},stopAutoSwitch:function(){clearTimeout(this.timeoutHandler)},startAutoSwitch:function(t){var e=this;this.autoSwitch=function(){e.previous=e.index,t&&"next"!=t?e.index>0?e.index--:e.index=e.max:e.index<e.max?e.index++:e.index=0,e.switchGallery(e.index,e.previous),e.nextTime=1e3*e.currentFrame.attr("Time")+1e3*e.effectTime,e.timeoutHandler=setTimeout(e.autoSwitch,parseInt(e.nextTime))},this.stopAutoSwitch(),this.autoSwitch()},switchNext:function(){this.startAutoSwitch("next")},switchPrevious:function(){this.startAutoSwitch("previous")},position:function(t,e){this.currentFrame.css("left",t),this.currentFrame.css("top",e)},resize:function(t,e){this.currentFrame.css("width",t),this.currentFrame.css("height",e)}};var posX=0,posY=0,lastPosX=0,lastPosY=0,lastWidth=0,lastHeight=0,scale=1,last_scale=1,rotation=0,last_rotation=0;