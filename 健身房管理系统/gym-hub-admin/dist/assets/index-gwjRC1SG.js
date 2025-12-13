import{S as pe,U as me,V as T,W as N,X as z,Y as w,Z as L,a0 as ge,a1 as M,d as P,a2 as _,a3 as fe,a4 as ve,a5 as _e,a6 as he,a7 as be,a8 as Q,a9 as j,aa as Ce,a as q,ab as ye,ac as S,ad as ee,r as W,ae as ze,af as we,ag as Se,ah as $e,ai as xe,aj as ke,ak as Re,T as Ie,al as Te,am as Ne,an as Pe,ao as Be,$ as x,n as qe,q as Ee,c as C,o as b,z as He,y as Ae,h as f,w as g,f as u,v as Le,s as We,e as H,B as V,g as k,t as y,x as F,ap as oe,b as ne,aq as D,L as Ve}from"./index-BAq2SbaD.js";import{u as Fe,a as O,s as je,b as Oe,v as Me,c as G,d as K,e as De}from"./auth-dh_SfOdj.js";function Ge(n){const{lineHeight:o,borderRadius:e,fontWeightStrong:t,baseColor:l,dividerColor:i,actionColor:a,textColor1:c,textColor2:s,closeColorHover:p,closeColorPressed:v,closeIconColor:h,closeIconColorHover:m,closeIconColorPressed:r,infoColor:d,successColor:R,warningColor:I,errorColor:B,fontSize:A}=n;return Object.assign(Object.assign({},me),{fontSize:A,lineHeight:o,titleFontWeight:t,borderRadius:e,border:`1px solid ${i}`,color:a,titleTextColor:c,iconColor:s,contentTextColor:s,closeBorderRadius:e,closeColorHover:p,closeColorPressed:v,closeIconColor:h,closeIconColorHover:m,closeIconColorPressed:r,borderInfo:`1px solid ${T(l,N(d,{alpha:.25}))}`,colorInfo:T(l,N(d,{alpha:.08})),titleTextColorInfo:c,iconColorInfo:d,contentTextColorInfo:s,closeColorHoverInfo:p,closeColorPressedInfo:v,closeIconColorInfo:h,closeIconColorHoverInfo:m,closeIconColorPressedInfo:r,borderSuccess:`1px solid ${T(l,N(R,{alpha:.25}))}`,colorSuccess:T(l,N(R,{alpha:.08})),titleTextColorSuccess:c,iconColorSuccess:R,contentTextColorSuccess:s,closeColorHoverSuccess:p,closeColorPressedSuccess:v,closeIconColorSuccess:h,closeIconColorHoverSuccess:m,closeIconColorPressedSuccess:r,borderWarning:`1px solid ${T(l,N(I,{alpha:.33}))}`,colorWarning:T(l,N(I,{alpha:.08})),titleTextColorWarning:c,iconColorWarning:I,contentTextColorWarning:s,closeColorHoverWarning:p,closeColorPressedWarning:v,closeIconColorWarning:h,closeIconColorHoverWarning:m,closeIconColorPressedWarning:r,borderError:`1px solid ${T(l,N(B,{alpha:.25}))}`,colorError:T(l,N(B,{alpha:.08})),titleTextColorError:c,iconColorError:B,contentTextColorError:s,closeColorHoverError:p,closeColorPressedError:v,closeIconColorError:h,closeIconColorHoverError:m,closeIconColorPressedError:r})}const Ke={common:pe,self:Ge},Ue=z("alert",`
 line-height: var(--n-line-height);
 border-radius: var(--n-border-radius);
 position: relative;
 transition: background-color .3s var(--n-bezier);
 background-color: var(--n-color);
 text-align: start;
 word-break: break-word;
`,[w("border",`
 border-radius: inherit;
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 transition: border-color .3s var(--n-bezier);
 border: var(--n-border);
 pointer-events: none;
 `),L("closable",[z("alert-body",[w("title",`
 padding-right: 24px;
 `)])]),w("icon",{color:"var(--n-icon-color)"}),z("alert-body",{padding:"var(--n-padding)"},[w("title",{color:"var(--n-title-text-color)"}),w("content",{color:"var(--n-content-text-color)"})]),ge({originalTransition:"transform .3s var(--n-bezier)",enterToProps:{transform:"scale(1)"},leaveToProps:{transform:"scale(0.9)"}}),w("icon",`
 position: absolute;
 left: 0;
 top: 0;
 align-items: center;
 justify-content: center;
 display: flex;
 width: var(--n-icon-size);
 height: var(--n-icon-size);
 font-size: var(--n-icon-size);
 margin: var(--n-icon-margin);
 `),w("close",`
 transition:
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 position: absolute;
 right: 0;
 top: 0;
 margin: var(--n-close-margin);
 `),L("show-icon",[z("alert-body",{paddingLeft:"calc(var(--n-icon-margin-left) + var(--n-icon-size) + var(--n-icon-margin-right))"})]),L("right-adjust",[z("alert-body",{paddingRight:"calc(var(--n-close-size) + var(--n-padding) + 2px)"})]),z("alert-body",`
 border-radius: var(--n-border-radius);
 transition: border-color .3s var(--n-bezier);
 `,[w("title",`
 transition: color .3s var(--n-bezier);
 font-size: 16px;
 line-height: 19px;
 font-weight: var(--n-title-font-weight);
 `,[M("& +",[w("content",{marginTop:"9px"})])]),w("content",{transition:"color .3s var(--n-bezier)",fontSize:"var(--n-font-size)"})]),w("icon",{transition:"color .3s var(--n-bezier)"})]),Xe=Object.assign(Object.assign({},j.props),{title:String,showIcon:{type:Boolean,default:!0},type:{type:String,default:"default"},bordered:{type:Boolean,default:!0},closable:Boolean,onClose:Function,onAfterLeave:Function,onAfterHide:Function}),U=P({name:"Alert",inheritAttrs:!1,props:Xe,setup(n){const{mergedClsPrefixRef:o,mergedBorderedRef:e,inlineThemeDisabled:t,mergedRtlRef:l}=Q(n),i=j("Alert","-alert",Ue,Ke,n,o),a=Ce("Alert",l,o),c=q(()=>{const{common:{cubicBezierEaseInOut:r},self:d}=i.value,{fontSize:R,borderRadius:I,titleFontWeight:B,lineHeight:A,iconSize:E,iconMargin:J,iconMarginRtl:te,closeIconSize:se,closeBorderRadius:ae,closeSize:re,closeMargin:le,closeMarginRtl:ie,padding:ce}=d,{type:$}=n,{left:de,right:ue}=ye(J);return{"--n-bezier":r,"--n-color":d[S("color",$)],"--n-close-icon-size":se,"--n-close-border-radius":ae,"--n-close-color-hover":d[S("closeColorHover",$)],"--n-close-color-pressed":d[S("closeColorPressed",$)],"--n-close-icon-color":d[S("closeIconColor",$)],"--n-close-icon-color-hover":d[S("closeIconColorHover",$)],"--n-close-icon-color-pressed":d[S("closeIconColorPressed",$)],"--n-icon-color":d[S("iconColor",$)],"--n-border":d[S("border",$)],"--n-title-text-color":d[S("titleTextColor",$)],"--n-content-text-color":d[S("contentTextColor",$)],"--n-line-height":A,"--n-border-radius":I,"--n-font-size":R,"--n-title-font-weight":B,"--n-icon-size":E,"--n-icon-margin":J,"--n-icon-margin-rtl":te,"--n-close-size":re,"--n-close-margin":le,"--n-close-margin-rtl":ie,"--n-padding":ce,"--n-icon-margin-left":de,"--n-icon-margin-right":ue}}),s=t?ee("alert",q(()=>n.type[0]),c,n):void 0,p=W(!0),v=()=>{const{onAfterLeave:r,onAfterHide:d}=n;r&&r(),d&&d()};return{rtlEnabled:a,mergedClsPrefix:o,mergedBordered:e,visible:p,handleCloseClick:()=>{var r;Promise.resolve((r=n.onClose)===null||r===void 0?void 0:r.call(n)).then(d=>{d!==!1&&(p.value=!1)})},handleAfterLeave:()=>{v()},mergedTheme:i,cssVars:t?void 0:c,themeClass:s==null?void 0:s.themeClass,onRender:s==null?void 0:s.onRender}},render(){var n;return(n=this.onRender)===null||n===void 0||n.call(this),_(be,{onAfterLeave:this.handleAfterLeave},{default:()=>{const{mergedClsPrefix:o,$slots:e}=this,t={class:[`${o}-alert`,this.themeClass,this.closable&&`${o}-alert--closable`,this.showIcon&&`${o}-alert--show-icon`,!this.title&&this.closable&&`${o}-alert--right-adjust`,this.rtlEnabled&&`${o}-alert--rtl`],style:this.cssVars,role:"alert"};return this.visible?_("div",Object.assign({},fe(this.$attrs,t)),this.closable&&_(ve,{clsPrefix:o,class:`${o}-alert__close`,onClick:this.handleCloseClick}),this.bordered&&_("div",{class:`${o}-alert__border`}),this.showIcon&&_("div",{class:`${o}-alert__icon`,"aria-hidden":"true"},_e(e.icon,()=>[_(ze,{clsPrefix:o},{default:()=>{switch(this.type){case"success":return _(xe,null);case"info":return _($e,null);case"warning":return _(Se,null);case"error":return _(we,null);default:return null}}})])),_("div",{class:[`${o}-alert-body`,this.mergedBordered&&`${o}-alert-body--bordered`]},he(e.header,l=>{const i=l||this.title;return i?_("div",{class:`${o}-alert-body__title`},i):null}),e.default&&_("div",{class:`${o}-alert-body__content`},e))):null}})}}),Ye=M([M("@keyframes spin-rotate",`
 from {
 transform: rotate(0);
 }
 to {
 transform: rotate(360deg);
 }
 `),z("spin-container",`
 position: relative;
 `,[z("spin-body",`
 position: absolute;
 top: 50%;
 left: 50%;
 transform: translateX(-50%) translateY(-50%);
 `,[ke()])]),z("spin-body",`
 display: inline-flex;
 align-items: center;
 justify-content: center;
 flex-direction: column;
 `),z("spin",`
 display: inline-flex;
 height: var(--n-size);
 width: var(--n-size);
 font-size: var(--n-size);
 color: var(--n-color);
 `,[L("rotate",`
 animation: spin-rotate 2s linear infinite;
 `)]),z("spin-description",`
 display: inline-block;
 font-size: var(--n-font-size);
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 margin-top: 8px;
 `),z("spin-content",`
 opacity: 1;
 transition: opacity .3s var(--n-bezier);
 pointer-events: all;
 `,[L("spinning",`
 user-select: none;
 -webkit-user-select: none;
 pointer-events: none;
 opacity: var(--n-opacity-spinning);
 `)])]),Ze={small:20,medium:18,large:16},Je=Object.assign(Object.assign({},j.props),{contentClass:String,contentStyle:[Object,String],description:String,stroke:String,size:{type:[String,Number],default:"medium"},show:{type:Boolean,default:!0},strokeWidth:Number,rotate:{type:Boolean,default:!0},spinning:{type:Boolean,validator:()=>!0,default:void 0},delay:Number}),X=P({name:"Spin",props:Je,setup(n){const{mergedClsPrefixRef:o,inlineThemeDisabled:e}=Q(n),t=j("Spin","-spin",Ye,Te,n,o),l=q(()=>{const{size:s}=n,{common:{cubicBezierEaseInOut:p},self:v}=t.value,{opacitySpinning:h,color:m,textColor:r}=v,d=typeof s=="number"?Ne(s):v[S("size",s)];return{"--n-bezier":p,"--n-opacity-spinning":h,"--n-size":d,"--n-color":m,"--n-text-color":r}}),i=e?ee("spin",q(()=>{const{size:s}=n;return typeof s=="number"?String(s):s[0]}),l,n):void 0,a=Pe(n,["spinning","show"]),c=W(!1);return Be(s=>{let p;if(a.value){const{delay:v}=n;if(v){p=window.setTimeout(()=>{c.value=!0},v),s(()=>{clearTimeout(p)});return}}c.value=a.value}),{mergedClsPrefix:o,active:c,mergedStrokeWidth:q(()=>{const{strokeWidth:s}=n;if(s!==void 0)return s;const{size:p}=n;return Ze[typeof p=="number"?"medium":p]}),cssVars:e?void 0:l,themeClass:i==null?void 0:i.themeClass,onRender:i==null?void 0:i.onRender}},render(){var n,o;const{$slots:e,mergedClsPrefix:t,description:l}=this,i=e.icon&&this.rotate,a=(l||e.description)&&_("div",{class:`${t}-spin-description`},l||((n=e.description)===null||n===void 0?void 0:n.call(e))),c=e.icon?_("div",{class:[`${t}-spin-body`,this.themeClass]},_("div",{class:[`${t}-spin`,i&&`${t}-spin--rotate`],style:e.default?"":this.cssVars},e.icon()),a):_("div",{class:[`${t}-spin-body`,this.themeClass]},_(Re,{clsPrefix:t,style:e.default?"":this.cssVars,stroke:this.stroke,"stroke-width":this.mergedStrokeWidth,class:`${t}-spin`}),a);return(o=this.onRender)===null||o===void 0||o.call(this),e.default?_("div",{class:[`${t}-spin-container`,this.themeClass],style:this.cssVars},_("div",{class:[`${t}-spin-content`,this.active&&`${t}-spin-content--spinning`,this.contentClass],style:this.contentStyle},e),_(Ie,{name:"fade-in-transition"},{default:()=>this.active?c:null})):c}}),Qe={class:"w-full flex-y-center gap-16px"},eo=P({name:"CaptchaVerification",__name:"captcha-verification",setup(n){const{loading:o,send:e,countdown:t}=Fe(je,{middleware:O("captcha:send")}),l=q(()=>t.value>0?x("page.login.codeLogin.reGetCode",{time:t.value}):x("page.login.codeLogin.getCode")),{form:i,loading:a,send:c}=Oe(m=>Me(m.phone,m.code),{initialForm:{phone:"",code:""}}),{formRef:s,validate:p}=qe(),v=q(()=>{const{formRules:m}=Ee();return{phone:m.phone,code:m.code}});async function h(){var m;await p(),await c(),(m=window.$message)==null||m.success(x("page.login.common.validateSuccess"))}return(m,r)=>{const d=Le,R=We,I=V,B=F,A=He;return b(),C(A,{ref_key:"formRef",ref:s,model:f(i),rules:v.value,size:"large","show-label":!1,onKeyup:Ae(h,["enter"])},{default:g(()=>[u(R,{path:"phone"},{default:g(()=>[u(d,{value:f(i).phone,"onUpdate:value":r[0]||(r[0]=E=>f(i).phone=E),placeholder:f(x)("page.login.common.phonePlaceholder"),maxlength:11},null,8,["value","placeholder"])]),_:1}),u(R,{path:"code"},{default:g(()=>[H("div",Qe,[u(d,{value:f(i).code,"onUpdate:value":r[1]||(r[1]=E=>f(i).code=E),placeholder:f(x)("page.login.common.codePlaceholder")},null,8,["value","placeholder"]),u(I,{size:"large",disabled:f(t)>0,loading:f(o),onClick:r[2]||(r[2]=E=>f(e)(f(i).phone))},{default:g(()=>[k(y(l.value),1)]),_:1},8,["disabled","loading"])])]),_:1}),u(B,{vertical:"",size:18,class:"w-full"},{default:g(()=>[u(I,{type:"primary",size:"large",round:"",block:"",loading:f(a),onClick:h},{default:g(()=>[k(y(f(x)("common.confirm")),1)]),_:1},8,["loading"])]),_:1})]),_:1},8,["model","rules"])}}}),oo={class:"inline-block",viewBox:"0 0 32 32",width:"1em",height:"1em"};function no(n,o){return b(),ne("svg",oo,[...o[0]||(o[0]=[H("path",{fill:"currentColor",d:"M24 8v16H8V8zm0-2H8a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V8a2 2 0 0 0-2-2"},null,-1)])])}const Y=oe({name:"carbon-stop",render:no}),to={class:"inline-block",viewBox:"0 0 32 32",width:"1em",height:"1em"};function so(n,o){return b(),ne("svg",to,[...o[0]||(o[0]=[H("path",{fill:"currentColor",d:"M7 28a1 1 0 0 1-1-1V5a1 1 0 0 1 1.482-.876l20 11a1 1 0 0 1 0 1.752l-20 11A1 1 0 0 1 7 28M8 6.69v18.62L24.925 16Z"},null,-1)])])}const Z=oe({name:"carbon-play",render:so}),ao=P({__name:"browser-visibility-request",setup(n){const o=G.Get("/mock/getLastTime",{cacheFor:null}),e=W(!1),{loading:t,data:l}=K(o,{enableVisibility:!0,enableNetwork:!1,enableFocus:!1,initialData:{time:""},async middleware(a,c){await O("autoRequest:1")(a,()=>Promise.resolve()),e.value||c()}}),i=()=>{e.value=!e.value};return(a,c)=>{const s=U,p=Z,v=Y,h=V,m=X,r=F;return b(),C(r,{vertical:""},{default:g(()=>[u(s,{type:"info"},{default:g(()=>[k(y(a.$t("page.alova.scenes.visibilityRequestTips")),1)]),_:1}),u(h,{type:"primary",onClick:i},{default:g(()=>[e.value?(b(),C(p,{key:0,class:"mr-2"})):(b(),C(v,{key:1,class:"mr-2"})),k(" "+y(e.value?a.$t("page.alova.scenes.startRequest"):a.$t("page.alova.scenes.stopRequest")),1)]),_:1}),u(r,{align:"center"},{default:g(()=>[H("span",null,y(a.$t("page.alova.scenes.refreshTime"))+": "+y(f(l).time||"--"),1),f(t)?(b(),C(m,{key:0,size:12})):D("",!0)]),_:1})]),_:1})}}}),ro=P({__name:"polling-request",setup(n){const o=G.Get("/mock/getLastTime",{cacheFor:null}),e=W(!1),{loading:t,data:l}=K(o,{pollingTime:3e3,initialData:{time:""},async middleware(a,c){await O("autoRequest:3")(a,()=>Promise.resolve()),e.value||c()}}),i=()=>{e.value=!e.value};return(a,c)=>{const s=U,p=Z,v=Y,h=V,m=X,r=F;return b(),C(r,{vertical:""},{default:g(()=>[u(s,{type:"info"},{default:g(()=>[k(y(a.$t("page.alova.scenes.pollingRequestTips")),1)]),_:1}),u(h,{type:"primary",onClick:i},{default:g(()=>[e.value?(b(),C(p,{key:0,class:"mr-2"})):(b(),C(v,{key:1,class:"mr-2"})),k(" "+y(e.value?a.$t("page.alova.scenes.startRequest"):a.$t("page.alova.scenes.stopRequest")),1)]),_:1}),u(r,{align:"center"},{default:g(()=>[H("span",null,y(a.$t("page.alova.scenes.refreshTime"))+": "+y(f(l).time||"--"),1),f(t)?(b(),C(m,{key:0,size:12})):D("",!0)]),_:1})]),_:1})}}}),lo=P({__name:"network-toggle-request",setup(n){const o=G.Get("/mock/getLastTime",{cacheFor:null}),e=W(!1),{loading:t,data:l}=K(o,{enableVisibility:!1,enableNetwork:!0,enableFocus:!1,initialData:{time:""},async middleware(a,c){await O("autoRequest:2")(a,()=>Promise.resolve()),e.value||c()}}),i=()=>{e.value=!e.value};return(a,c)=>{const s=U,p=Z,v=Y,h=V,m=X,r=F;return b(),C(r,{vertical:""},{default:g(()=>[u(s,{type:"info"},{default:g(()=>[k(y(a.$t("page.alova.scenes.networkRequestTips")),1)]),_:1}),u(h,{type:"primary",onClick:i},{default:g(()=>[e.value?(b(),C(p,{key:0,class:"mr-2"})):(b(),C(v,{key:1,class:"mr-2"})),k(" "+y(e.value?a.$t("page.alova.scenes.startRequest"):a.$t("page.alova.scenes.stopRequest")),1)]),_:1}),u(r,{align:"center"},{default:g(()=>[H("span",null,y(a.$t("page.alova.scenes.refreshTime"))+": "+y(f(l).time||"--"),1),f(t)?(b(),C(m,{key:0,size:12})):D("",!0)]),_:1})]),_:1})}}}),io=P({__name:"cross-component-request",setup(n){const o=async()=>{De(/^autoRequest/,async({send:e})=>{await e()})};return(e,t)=>{const l=V;return b(),C(l,{onClick:o},{default:g(()=>[k(y(e.$t("page.alova.scenes.triggerAllRequest")),1)]),_:1})}}}),po=P({name:"alova_scenes",__name:"index",setup(n){return(o,e)=>{const t=Ve,l=F;return b(),C(l,{vertical:"",size:16},{default:g(()=>[u(t,{title:f(x)("page.alova.scenes.captchaSend"),bordered:!1,size:"small",segmented:"",class:"card-wrapper"},{default:g(()=>[u(eo,{class:"w-1/3"})]),_:1},8,["title"]),u(t,{title:f(x)("page.alova.scenes.autoRequest"),bordered:!1,size:"small",segmented:"",class:"card-wrapper"},{default:g(()=>[u(l,{wrap:!1},{default:g(()=>[u(ao),u(ro),u(lo)]),_:1})]),_:1},8,["title"]),u(t,{title:f(x)("page.alova.scenes.requestCrossComponent"),bordered:!1,size:"small",segmented:"",class:"card-wrapper"},{default:g(()=>[u(io)]),_:1},8,["title"])]),_:1})}}});export{po as default};
