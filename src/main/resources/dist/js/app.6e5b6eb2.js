(function(e){function t(t){for(var s,a,i=t[0],l=t[1],u=t[2],m=0,d=[];m<i.length;m++)a=i[m],Object.prototype.hasOwnProperty.call(o,a)&&o[a]&&d.push(o[a][0]),o[a]=0;for(s in l)Object.prototype.hasOwnProperty.call(l,s)&&(e[s]=l[s]);c&&c(t);while(d.length)d.shift()();return n.push.apply(n,u||[]),r()}function r(){for(var e,t=0;t<n.length;t++){for(var r=n[t],s=!0,i=1;i<r.length;i++){var l=r[i];0!==o[l]&&(s=!1)}s&&(n.splice(t--,1),e=a(a.s=r[0]))}return e}var s={},o={app:0},n=[];function a(t){if(s[t])return s[t].exports;var r=s[t]={i:t,l:!1,exports:{}};return e[t].call(r.exports,r,r.exports,a),r.l=!0,r.exports}a.m=e,a.c=s,a.d=function(e,t,r){a.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:r})},a.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},a.t=function(e,t){if(1&t&&(e=a(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var r=Object.create(null);if(a.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var s in e)a.d(r,s,function(t){return e[t]}.bind(null,s));return r},a.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return a.d(t,"a",t),t},a.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},a.p="";var i=window["webpackJsonp"]=window["webpackJsonp"]||[],l=i.push.bind(i);i.push=t,i=i.slice();for(var u=0;u<i.length;u++)t(i[u]);var c=l;n.push([0,"chunk-vendors"]),r()})({0:function(e,t,r){e.exports=r("56d7")},"154d":function(e,t,r){},"1bcf":function(e,t,r){},"21af":function(e,t,r){},2940:function(e,t,r){"use strict";var s=r("1bcf"),o=r.n(s);o.a},"40b4":function(e,t,r){},"51e2":function(e,t,r){"use strict";var s=r("549d"),o=r.n(s);o.a},"549d":function(e,t,r){},"56d7":function(e,t,r){"use strict";r.r(t);r("e260"),r("e6cf"),r("cca6"),r("a79d");var s=r("2b0e"),o=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{attrs:{id:"app"}},[r("router-view")],1)},n=[],a={created:function(){this.$globl.isNeedZoom&&.9!=document.body.style.zoom&&(document.body.style.zoom=.9)}},i=a,l=(r("5c0b"),r("2877")),u=Object(l["a"])(i,o,n,!1,null,null,null),c=u.exports,m=(r("caad"),r("a9e3"),r("2532"),r("8c4f")),d=function(){var e=this,t=e.$createElement;e._self._c;return e._m(0)},f=[function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("h1",[e._v("你没有权限访问!")])])}],p={name:"NotFound"},g=p,h=Object(l["a"])(g,d,f,!1,null,"698e213c",null),b=h.exports,v=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"home"},[r("header",{staticClass:"header"},[r("div",{staticClass:"left"},[e._v(" AI服务平台 "),r("el-button",{attrs:{circle:"",icon:e.menuCollapse?"el-icon-s-unfold":"el-icon-s-fold"},on:{click:e.onMenuCollapse}})],1),r("div",{staticClass:"right",staticStyle:{display:"flex","flex-direction":"row","align-items":"center"}},[r("div",{staticStyle:{"margin-right":"30px","font-size":"12px",color:"rgba(255,255,255,0.78)"}},[r("el-link",{nativeOn:{click:function(t){return e.setIsNeedZoom(t)}}},[e._v("显示异常?点击修复视图")])],1),r("div",{staticClass:"warp",staticStyle:{"margin-right":"auto"}},[r("el-dropdown",[r("span",{staticClass:"el-dropdown-link"},[r("el-avatar",{attrs:{shape:"circle",size:40,src:e.userphoto}})],1),r("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[r("el-dropdown-item",[e._v(e._s(e.name))]),r("el-dropdown-item",[e._v("切换账号")]),r("el-dropdown-item",{nativeOn:{click:function(t){return e.onLogOut(t)}}},[e._v("退出登录")])],1)],1)],1)])]),r("section",{staticClass:"section"},[r("div",{staticClass:"tac"},[r("div",{staticClass:"col1"},[r("el-menu",{staticClass:"el-menu-vertical-demo",attrs:{"default-active":"2",collapse:e.menuCollapse,"unique-opened":!0,"default-openeds":e.defaultUnfoldedMenu,select:"1-1","background-color":"#545c64","text-color":"#fff","active-text-color":"#ffd04b"},on:{open:e.handleOpen,close:e.handleClose}},e._l(e.menuData,(function(t){return r("div",{key:t.index},[t.childList&&t.childList.length>0?r("el-submenu",{attrs:{index:t.index}},[r("template",{slot:"title"},[r("i",{class:t.iconClassName}),r("span",{attrs:{slot:"title"},slot:"title"},[e._v(e._s(e.menuCollapse?"":t.optionName))])]),e._l(t.childList,(function(t){return r("el-menu-item",{key:t.index,attrs:{index:t.index,routerName:t.routerName},on:{click:function(r){return e.onClickMenu(t)}}},[r("template",{slot:"title"},[r("i",{class:t.iconClassName}),r("span",{attrs:{slot:"title"},slot:"title"},[e._v(e._s(e.menuCollapse?"":t.optionName))])])],2)}))],2):r("el-menu-item",{attrs:{index:t.index,disabled:t.disabled,routerName:t.routerName},on:{click:function(r){return e.onClickMenu(t)}}},[r("i",{class:t.iconClassName}),r("span",{attrs:{slot:"title"},slot:"title"},[e._v(e._s(e.menuCollapse?"":t.optionName))])])],1)})),0)],1),r("div",{staticClass:"col2"},[r("header",{staticClass:"col2_header"},e._l(e.tags,(function(t){return r("el-tag",{key:t.routerName,staticClass:"item",attrs:{closable:"",type:t.type?t.type:"info"},on:{close:function(r){return e.close(t)},click:function(r){return e.clickTag(t)}}},[e._v(e._s(t.name))])})),1),r("div",{staticClass:"warp"},[r("router-view")],1)])])])])},w=[],x=(r("7db0"),r("c740"),r("4160"),r("a434"),r("b0c0"),r("159b"),r("96cf"),r("1da1")),y=r("bc3a"),S=r.n(y);r("d3b7"),r("ac1f"),r("5319");function k(e){e.interceptors.request.use((function(e){return e.headers["Content-Type"]="application/json",e.headers.Authorization=localStorage.getItem("token"),e.headers.type="back",e.headers.userid=localStorage.getItem("userid"),e}),(function(e){Promise.reject(e)})),e.interceptors.response.use((function(e){switch(console.log(e),e.data.status){case 900:sessionStorage.clear(),localStorage.clear(),ie.replace({path:"/login",query:{redirect:ie.currentRoute.value.fullPath}})}switch(e.status){case 401:sessionStorage.clear(),localStorage.clear(),ie.replace({path:"/login",query:{redirect:ie.currentRoute.fullPath}})}return e.data}),(function(e){switch(e.response.status){case 400:e.message="请求错误(400)";break;case 401:e.message="未授权，请重新登录(401)",sessionStorage.clear(),localStorage.clear(),ie.replace({path:"/login",query:{redirect:ie.currentRoute.fullPath}});break;case 900:e.message="未授权，请重新登录(900)",sessionStorage.clear(),localStorage.clear(),ie.replace({path:"/login",query:{redirect:ie.currentRoute.fullPath}});break;case 403:e.message="拒绝访问(403)";break;case 404:e.message="请求出错(404)";break;case 408:e.message="请求超时(408)";break;case 500:e.message="服务器错误(500)";break;case 501:e.message="服务未实现(501)";break;case 502:e.message="网络错误(502)";break;case 503:e.message="服务不可用(503)";break;case 504:e.message="网络超时(504)";break;case 505:e.message="HTTP版本不受支持(505)";break}}))}var N=S.a.create({baseURL:"/api",timeout:2e4});k(N);var I=N;function _(e,t){return I({url:"/user/login",method:"post",data:{username:e,password:t}})}function C(){return I({url:"/user/logout",method:"post"})}function O(){return I({url:"/user/loginbytoken",method:"post",data:{}})}function $(e,t,r,s,o,n){return I({url:"/user/updataforuser",method:"post",data:{id:String(e),username:String(t),name:String(r),sex:String(s),idNumber:String(o),phone:String(n)}})}function q(e,t,r,s,o){return I({url:"/user/changepassword",method:"post",data:{id:String(e),username:String(t),password:String(r),newpassword:String(s),checknewpassword:String(o)}})}var j={name:"index",data:function(){return{menuData:[{optionName:"我的主页",iconClassName:"el-icon-eleme",index:"1",disabled:!1,childList:[{optionName:"个人信息",index:"1-1",routerName:"userinfo",iconClassName:"el-icon-user"},{optionName:"修改密码",index:"1-4",routerName:"changepassword",iconClassName:"el-icon-lock"}]},{optionName:"打印服务",iconClassName:"el-icon-setting",index:"2",disabled:!1,childList:[{optionName:"打印页",index:"2-1",routerName:"printindex",iconClassName:"el-icon-s-shop"}]}],menuCollapse:!1,defaultUnfoldedMenu:"1",tags:[],userphoto:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",showPhotoMenu:!1,name:"11",userInfo:{}}},created:function(){this.defaultUnfoldedMenu=[localStorage.getItem("defaultUnfoldedMenu")],this.menuCollapse=JSON.parse(localStorage.getItem("menuCollapse")),this.tags=JSON.parse(localStorage.getItem("tagsNavList"))||[];var e=window.localStorage.getItem("userInfo");e&&(this.userInfo=JSON.parse(e)),this.name=this.userInfo.name},methods:{onMenuCollapse:function(){this.menuCollapse=!this.menuCollapse,localStorage.setItem("menuCollapse",this.menuCollapse),this.defaultUnfoldedMenu=[localStorage.getItem("defaultUnfoldedMenu")]},handleOpen:function(e,t){console.log(e,t),localStorage.setItem("defaultUnfoldedMenu",""+e)},handleClose:function(e,t){console.log(e,t)},onClickMenu:function(e){var t=this;if(this.$router.history.current.name!=e.routerName){this.$router.push({name:e.routerName});var r=s(e.routerName);if(r)return this.tags.forEach((function(e){e.type="info"})),void(r.type="success");this.tags.forEach((function(e){e.type="info"})),this.tags.push({name:e.optionName,routerName:e.routerName,type:"success"}),this.changeTagColor(e.routerName),localStorage.setItem("tagsNavList",JSON.stringify(this.tags))}function s(e){var r=t.tags;return r.find((function(t){return t.routerName==e}))}},clickTag:function(e){console.log(e.routerName),this.$router.history.current.name!=e.routerName&&(this.$router.push({name:e.routerName}),this.changeTagColor(e.routerName),localStorage.setItem("tagsNavList",JSON.stringify(this.tags)))},close:function(e){console.log(e);var t=this.tags.findIndex((function(t){return t.routerName==e.routerName}));console.log(t),this.tags.splice(t,1),localStorage.setItem("tagsNavList",JSON.stringify(this.tags))},changeTagColor:function(e){this.tags.forEach((function(t){t.routerName==e?t.type="success":t.type="info"}))},setIsNeedZoom:function(){this.$globl.isNeedZoom=!0,document.body.style.zoom=.9},onLogOut:function(){var e=this;return Object(x["a"])(regeneratorRuntime.mark((function t(){var r;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,C();case 2:r=t.sent,console.log(r),"900"===String(r.code)?(e.$message.success(r.msg),sessionStorage.clear(),localStorage.clear(),e.$router.push("login")):701===r.status&&e.$message.error(r.msg);case 5:case"end":return t.stop()}}),t)})))()}}},E=j,T=(r("51e2"),Object(l["a"])(E,v,w,!1,null,"200c1018",null)),R=T.exports,L=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"login",attrs:{id:"login-app"}},[r("div",{staticClass:"login-box"},[r("img",{attrs:{src:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",alt:""}}),r("div",{staticClass:"login-form"},[r("el-form",{ref:"loginForm",attrs:{model:e.loginForm,rules:e.loginRules}},[r("div",{staticClass:"login-form-title"},[r("span",[e._v("瑞吉外卖")])]),r("el-form-item",{attrs:{prop:"username"}},[r("el-input",{attrs:{type:"text","auto-complete":"off",placeholder:"账号",maxlength:"20","prefix-icon":"iconfont icon-user"},model:{value:e.loginForm.username,callback:function(t){e.$set(e.loginForm,"username",t)},expression:"loginForm.username"}})],1),r("el-form-item",{attrs:{prop:"password"}},[r("el-input",{attrs:{type:"password",placeholder:"密码","prefix-icon":"iconfont icon-lock",maxlength:"20"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleLogin(t)}},model:{value:e.loginForm.password,callback:function(t){e.$set(e.loginForm,"password",t)},expression:"loginForm.password"}})],1),r("el-form-item",{staticStyle:{width:"100%"}},[r("el-button",{staticClass:"login-btn",staticStyle:{width:"100%"},attrs:{loading:e.loading,size:"medium",type:"primary"},nativeOn:{click:function(t){return t.preventDefault(),e.handleLogin(t)}}},[e.loading?r("span",[e._v("登录中...")]):r("span",[e._v("登录")])])],1)],1)],1)])])},M=[],P={name:"login",el:"#login-app",data:function(){return{loginForm:{username:"admin",password:"123456"},loading:!1}},computed:{loginRules:function(){var e=function(e,t,r){t.length<1?r(new Error("请输入用户名")):r()},t=function(e,t,r){t.length<6?r(new Error("密码必须在6位以上")):r()};return{username:[{validator:e,trigger:"blur"}],password:[{validator:t,trigger:"blur"}]}}},created:function(){this.checkToken()},methods:{handleLogin:function(){var e=this;return Object(x["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:e.$refs.loginForm.validate(function(){var t=Object(x["a"])(regeneratorRuntime.mark((function t(r){var s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(!r){t.next=6;break}return e.loading=!0,t.next=4,_(e.loginForm.username,e.loginForm.password);case 4:s=t.sent,"1"===String(s.code)?(localStorage.setItem("userInfo",JSON.stringify(s.data)),localStorage.setItem("type",s.data.permissions),localStorage.setItem("userid",String(s.data.id)),localStorage.setItem("token",s.data.token),ie.push({name:"index"})):(e.$message.error(s.msg),sessionStorage.setItem("userLastStoreId",""),e.loading=!1);case 6:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}());case 1:case"end":return t.stop()}}),t)})))()},checkToken:function(){return Object(x["a"])(regeneratorRuntime.mark((function e(){var t;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,O();case 2:t=e.sent,"1"===String(t.code)?(console.log("验证"),localStorage.setItem("type",t.data.permissions),localStorage.setItem("userInfo",JSON.stringify(t.data)),localStorage.setItem("userid",String(t.data.id)),ie.push({name:"index"})):sessionStorage.setItem("userLastStoreId","");case 4:case"end":return e.stop()}}),e)})))()}}},A=P,F=(r("e66f"),Object(l["a"])(A,L,M,!1,null,"5fade74e",null)),J=F.exports,z=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("el-card",{staticClass:"box-card"},[r("el-form",{attrs:{model:e.form,"label-width":"120px"}},[r("el-form-item",{attrs:{rules:[{required:!0,message:"id is required"}],label:"id"}},[r("el-input",{attrs:{disabled:""},model:{value:e.form.id,callback:function(t){e.$set(e.form,"id",t)},expression:"form.id"}})],1),r("el-form-item",{attrs:{rules:[{required:!0,message:"用户名 is required"}],label:"用户名"}},[r("el-input",{attrs:{disabled:""},model:{value:e.form.username,callback:function(t){e.$set(e.form,"username",t)},expression:"form.username"}})],1),r("el-form-item",{attrs:{rules:[{required:!0,message:"昵称 is required"}],label:"昵称"}},[r("el-input",{model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),r("el-form-item",{attrs:{rules:[{required:!0,message:"性别 is required"}],label:"性别"}},[r("el-select",{attrs:{placeholder:"请选择你的性别"},model:{value:e.form.sex,callback:function(t){e.$set(e.form,"sex",t)},expression:"form.sex"}},[r("el-option",{attrs:{label:"男",value:"男"}}),r("el-option",{attrs:{label:"女",value:"女"}})],1)],1),r("el-form-item",{attrs:{rules:[{required:!0,message:"idNumber is required"},{type:"number",message:"idNumber must be a number"}],label:"身份证号码"}},[r("el-input",{model:{value:e.form.idNumber,callback:function(t){e.$set(e.form,"idNumber",t)},expression:"form.idNumber"}})],1),r("el-form-item",{attrs:{rules:[{required:!0,message:"手机号码必须填写"},{type:"number",message:"手机号码必须是数字"}],label:"手机号码"}},[r("el-input",{model:{value:e.form.phone,callback:function(t){e.$set(e.form,"phone",t)},expression:"form.phone"}})],1),r("el-form-item",[r("el-button",{attrs:{type:"primary",disabled:e.form_serve.id===e.form.id&&e.form_serve.username===e.form.username&&e.form_serve.name===e.form.name&&e.form_serve.sex===e.form.sex&&e.form_serve.idNumber===e.form.idNumber&&e.form_serve.phone===e.form.phone},on:{click:e.onSubmit}},[e._v("更新")]),r("el-button",{on:{click:e.reWrite}},[e._v("重置")])],1)],1)],1)],1)},U=[],B={name:"UserInfo",data:function(){return{form:{id:"",username:"",name:"",sex:"",idNumber:"",phone:""},form_serve:{id:"",username:"",name:"",sex:"",idNumber:"",phone:""},userInfo:{}}},created:function(){this.init()},methods:{reWrite:function(){this.form.name=this.userInfo.name,this.form.phone=this.userInfo.phone,this.form.sex=this.userInfo.sex,this.form.username=this.userInfo.username,this.form.idNumber=this.userInfo.idNumber,this.form.id=this.userInfo.id,this.form_serve.name=this.userInfo.name,this.form_serve.phone=this.userInfo.phone,this.form_serve.sex=this.userInfo.sex,this.form_serve.username=this.userInfo.username,this.form_serve.idNumber=this.userInfo.idNumber,this.form_serve.id=this.userInfo.id},init:function(){var e=this.$loading({lock:!0,text:"Loading",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"}),t=localStorage.getItem("userInfo");t?(this.userInfo=JSON.parse(t),this.form.name=this.userInfo.name,this.form.phone=this.userInfo.phone,this.form.sex=this.userInfo.sex,this.form.username=this.userInfo.username,this.form.idNumber=this.userInfo.idNumber,this.form.id=this.userInfo.id,this.form_serve.name=this.userInfo.name,this.form_serve.phone=this.userInfo.phone,this.form_serve.sex=this.userInfo.sex,this.form_serve.username=this.userInfo.username,this.form_serve.idNumber=this.userInfo.idNumber,this.form_serve.id=this.userInfo.id,e.close()):this.$message.error("数据错误,请刷新重试!")},onSubmit:function(){var e=this;return Object(x["a"])(regeneratorRuntime.mark((function t(){var r,s,o;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return r=e,t.next=3,$(r.form.id,r.form.username,r.form.name,r.form.sex,r.form.idNumber,r.form.phone);case 3:if(s=t.sent,"1"!==String(s.code)){t.next=12;break}return r.$message.success(s.msg),t.next=8,O();case 8:o=t.sent,"1"===String(o.code)?(localStorage.setItem("userInfo",JSON.stringify(o.data)),r.init()):(e.$message.error(o.msg),ie.push({name:"index"})),t.next=13;break;case 12:e.$message.error(s.msg);case 13:case"end":return t.stop()}}),t)})))()}}},Z=B,D=(r("7bc7"),Object(l["a"])(Z,z,U,!1,null,"819bbd10",null)),W=D.exports,H=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{attrs:{id:"changepassword"}},[r("el-card",{staticClass:"box-card"},[r("el-form",{ref:"passform",attrs:{model:e.passform,rules:e.passrules,"label-position":"left","label-width":"80px"}},[r("el-form-item",{attrs:{label:"原密码",prop:"password"}},[r("el-input",{attrs:{type:"password",placeholder:"请输入原密码"},model:{value:e.passform.password,callback:function(t){e.$set(e.passform,"password",t)},expression:"passform.password"}})],1),r("el-form-item",{attrs:{label:"新密码",prop:"newpassword"}},[r("el-input",{attrs:{id:"inputValue",type:"password",placeholder:"请输入新密码"},model:{value:e.passform.newpassword,callback:function(t){e.$set(e.passform,"newpassword",t)},expression:"passform.newpassword"}})],1),r("el-form-item",{staticStyle:{"text-align":"center"},attrs:{label:"密码强度"}},[r("div",{staticClass:"input_span"},[r("span",{staticStyle:{background:"#eee"},attrs:{id:"one"}}),r("span",{staticStyle:{background:"#eee"},attrs:{id:"two"}}),r("span",{staticStyle:{background:"#eee"},attrs:{id:"three"}})]),r("div",{},[r("span",{staticStyle:{"padding-right":"15rem"}},[e._v("弱")]),r("span",{staticStyle:{"padding-right":"15rem"}},[e._v("中")]),r("span",[e._v("强")])])]),r("el-form-item",{attrs:{label:"确认新密码",prop:"checkpassword","label-width":"120px"}},[r("el-input",{attrs:{type:"password",placeholder:"请确认新密码"},model:{value:e.passform.checkpassword,callback:function(t){e.$set(e.passform,"checkpassword",t)},expression:"passform.checkpassword"}})],1),r("el-form-item",[r("el-button",{attrs:{type:"primary",disabled:""==e.passform.password||e.passform.newpassword!=e.passform.checkpassword||""==e.passform.newpassword||""==e.passform.checkpassword},on:{click:e.onSubmit}},[e._v("提交修改")])],1)],1)],1)],1)},V=[],G={name:"ChangePassword",data:function(){var e=this,t=function(t,r,s){r!==e.passform.newpassword?s(new Error("请确认新密码")):s()};return{msgText:"",passform:{password:"",newpassword:"",checkpassword:""},userInfo:{},passrules:{password:[{required:!0,message:"请输入原密码",trigger:"blur"}],newpassword:[{required:!0,message:"请输入新密码",trigger:"blur"},{min:4,max:20,trigger:"blur",message:"密码长度为4到20位"}],checkpassword:[{required:!0,message:"请输入新密码",trigger:"blur"},{validator:t}]}}},watch:{"passform.newpassword":{handler:function(e){this.msgText=this.checkStrong(e),this.msgText>1||1==this.msgText?(document.getElementById("one").style.background="#ff0000",console.log("red")):document.getElementById("one").style.background="#eee",this.msgText>2||2==this.msgText?document.getElementById("two").style.background="orange":document.getElementById("two").style.background="#eee",4==this.msgText?document.getElementById("three").style.background="#00D1B2":document.getElementById("three").style.background="#eee"}}},created:function(){var e=localStorage.getItem("userInfo");e&&(this.userInfo=JSON.parse(e))},methods:{checkStrong:function(e){var t=0;if(e.length<1)return t;switch(/\d/.test(e)&&t++,/[a-z]/.test(e)&&t++,/[A-Z]/.test(e)&&t++,/\W/.test(e)&&t++,t){case 1:return 1;case 2:return 2;case 3:case 4:return e.length<4?3:4}return t},onSubmit:function(){var e=this;return Object(x["a"])(regeneratorRuntime.mark((function t(){var r,s,o,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return r=e,s=r.userInfo.id,console.log(s),o=new String(s),t.next=6,q(o,r.userInfo.username,r.passform.password,r.passform.newpassword,r.passform.checkpassword);case 6:n=t.sent,"1"===String(n.code)?(r.$message.success(n.msg),localStorage.clear(),sessionStorage.clear(),ie.push("/login")):r.$message.error(n.msg);case 8:case"end":return t.stop()}}),t)})))()}}},K=G,Q=(r("2940"),Object(l["a"])(K,H,V,!1,null,null,null)),X=Q.exports,Y=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[e._v(" 跨域暂时没解决,需要借助nginx "),r("el-upload",{ref:"upload",staticClass:"upload-demo",attrs:{action:"http://192.168.12.122:8080/print/uploadpdf",headers:e.headerObj,"on-success":e.handleAvatarSuccess,"before-upload":e.beforeUpload}},[r("el-button",{attrs:{size:"small",type:"primary"}},[e._v("点击打印")]),r("div",{staticClass:"el-upload__tip",attrs:{slot:"tip"},slot:"tip"},[e._v("只能上传pdf文件")])],1)],1)},ee=[],te=(r("1276"),{name:"Print.vue",data:function(){return{headerObj:{Authorization:localStorage.getItem("token"),userid:localStorage.getItem("userid")}}},created:function(){},methods:{handleAvatarSuccess:function(e,t,r){console.log(t),console.log(r),"1"===String(e.code)?this.$message.success(e.msg):this.$message.error(e.msg),this.isimageupload=!1,this.$refs.upload.clearFiles()},beforeUpload:function(e){this.isimageupload=!0;var t=e.name.split(".")[1],r=e.size/1024/1024<8;return["pdf"].includes(String(t))?r?void 0:(this.$message.error("上传文件大小超过 8MB"),!1):(this.$message.error("上传只支持 pdf"),this.$refs.upload.clearFiles(),!1)}}}),re=te,se=Object(l["a"])(re,Y,ee,!1,null,"6fdae150",null),oe=se.exports,ne=[{path:"/",component:c,meta:{requireAuth:!1,needrole:!1},children:[{path:"",redirect:"/login",meta:{requireAuth:!1,needrole:!1}}]},{path:"/login",name:"login",component:J,meta:{requireAuth:!1,needrole:!1}},{path:"/404",name:"NotFound",component:b,meta:{requireAuth:!1,needrole:!1}},{path:"/index",name:"index",component:R,meta:{requireAuth:!1,needrole:!1},children:[{path:"/userinfo",name:"userinfo",component:W,meta:{requireAuth:!1,needrole:!1}},{path:"/changepassword",name:"changepassword",component:X,meta:{requireAuth:!1,needrole:!1}},{path:"/printindex",name:"printindex",component:oe,meta:{requireAuth:!1,needrole:!1}}]},{path:"*",name:"*",redirect:"/404",meta:{requireAuth:!1,needrole:!1}}];s["default"].use(m["a"]);var ae=new m["a"]({mode:"hash",routes:ne});ae.beforeEach((function(e,t,r){if(console.log("to"),console.log(e),r(),e.meta.requireAuth)if(localStorage.getItem("token")){if(e.meta.needrole){console.log(e.meta.roles),console.log(localStorage.getItem("type"));var s=e.meta.roles,o=localStorage.getItem("type");console.log(s.includes(Number(o))),s.includes(Number(o))?(console.log("存在"),r()):ae.push({path:"/404"})}r()}else r({path:"/login",query:{redirect:e.fullPath}});else r()}));var ie=ae,le=r("2f62");s["default"].use(le["a"]);var ue=new le["a"].Store({state:{},mutations:{},actions:{},modules:{}}),ce=r("5c96"),me=r.n(ce),de=(r("0fae"),r("21af"),r("b751"),{isNeedZoom:!1});s["default"].config.productionTip=!1,s["default"].prototype.$globl=de,s["default"].use(me.a),new s["default"]({router:ie,store:ue,render:function(e){return e(c)}}).$mount("#app")},"5c0b":function(e,t,r){"use strict";var s=r("9c0c"),o=r.n(s);o.a},"7bc7":function(e,t,r){"use strict";var s=r("40b4"),o=r.n(s);o.a},"9c0c":function(e,t,r){},b751:function(e,t,r){},e66f:function(e,t,r){"use strict";var s=r("154d"),o=r.n(s);o.a}});
//# sourceMappingURL=app.6e5b6eb2.js.map