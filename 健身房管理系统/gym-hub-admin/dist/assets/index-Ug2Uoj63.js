import{_ as ye,N as xe}from"./table-header-operation.vue_vue_type_script_setup_true_lang-BvwCvuJW.js";import{a1 as d,X,bt as we,bu as ke,Z as G,a$ as Ce,d as W,a2 as Ne,a8 as Te,a9 as ne,cP as Se,aa as ze,a as Q,ac as Z,ad as Pe,aI as A,aJ as qe,at as Y,m as oe,E as le,au as ae,n as se,q as de,r as p,av as Be,c as ie,o as V,w as r,f as e,aK as ue,e as v,b as ee,P as De,Q as Oe,x as te,B as L,g as B,t as k,h as a,$ as P,aL as ce,z as Re,az as $e,aA as Me,ax as Ie,v as Ue,L as pe,ar as je,aC as J,aD as Le}from"./index-BAq2SbaD.js";import{f as me}from"./user-CToCrV-S.js";import{u as Ee,a as Fe}from"./table-DmjEK7tG.js";import{f as Ve}from"./orderDetail-CXOQ_ZcE.js";import{_ as Ae}from"./Image-Bgw24gBA.js";import{_ as Ke,a as He,b as Ge,c as Je}from"./round-refresh-DNCv4Ss6.js";import{_ as Qe}from"./DataTable-D6rjIJFQ.js";import"./RadioGroup-BGb2VClf.js";import"./Pagination-PYMt0YJG.js";const We=d([X("table",`
 font-size: var(--n-font-size);
 font-variant-numeric: tabular-nums;
 line-height: var(--n-line-height);
 width: 100%;
 border-radius: var(--n-border-radius) var(--n-border-radius) 0 0;
 text-align: left;
 border-collapse: separate;
 border-spacing: 0;
 overflow: hidden;
 background-color: var(--n-td-color);
 border-color: var(--n-merged-border-color);
 transition:
 background-color .3s var(--n-bezier),
 border-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 --n-merged-border-color: var(--n-border-color);
 `,[d("th",`
 white-space: nowrap;
 transition:
 background-color .3s var(--n-bezier),
 border-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 text-align: inherit;
 padding: var(--n-th-padding);
 vertical-align: inherit;
 text-transform: none;
 border: 0px solid var(--n-merged-border-color);
 font-weight: var(--n-th-font-weight);
 color: var(--n-th-text-color);
 background-color: var(--n-th-color);
 border-bottom: 1px solid var(--n-merged-border-color);
 border-right: 1px solid var(--n-merged-border-color);
 `,[d("&:last-child",`
 border-right: 0px solid var(--n-merged-border-color);
 `)]),d("td",`
 transition:
 background-color .3s var(--n-bezier),
 border-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 padding: var(--n-td-padding);
 color: var(--n-td-text-color);
 background-color: var(--n-td-color);
 border: 0px solid var(--n-merged-border-color);
 border-right: 1px solid var(--n-merged-border-color);
 border-bottom: 1px solid var(--n-merged-border-color);
 `,[d("&:last-child",`
 border-right: 0px solid var(--n-merged-border-color);
 `)]),G("bordered",`
 border: 1px solid var(--n-merged-border-color);
 border-radius: var(--n-border-radius);
 `,[d("tr",[d("&:last-child",[d("td",`
 border-bottom: 0 solid var(--n-merged-border-color);
 `)])])]),G("single-line",[d("th",`
 border-right: 0px solid var(--n-merged-border-color);
 `),d("td",`
 border-right: 0px solid var(--n-merged-border-color);
 `)]),G("single-column",[d("tr",[d("&:not(:last-child)",[d("td",`
 border-bottom: 0px solid var(--n-merged-border-color);
 `)])])]),G("striped",[d("tr:nth-of-type(even)",[d("td","background-color: var(--n-td-color-striped)")])]),Ce("bottom-bordered",[d("tr",[d("&:last-child",[d("td",`
 border-bottom: 0px solid var(--n-merged-border-color);
 `)])])])]),we(X("table",`
 background-color: var(--n-td-color-modal);
 --n-merged-border-color: var(--n-border-color-modal);
 `,[d("th",`
 background-color: var(--n-th-color-modal);
 `),d("td",`
 background-color: var(--n-td-color-modal);
 `)])),ke(X("table",`
 background-color: var(--n-td-color-popover);
 --n-merged-border-color: var(--n-border-color-popover);
 `,[d("th",`
 background-color: var(--n-th-color-popover);
 `),d("td",`
 background-color: var(--n-td-color-popover);
 `)]))]),Xe=Object.assign(Object.assign({},ne.props),{bordered:{type:Boolean,default:!0},bottomBordered:{type:Boolean,default:!0},singleLine:{type:Boolean,default:!0},striped:Boolean,singleColumn:Boolean,size:{type:String,default:"medium"}}),Ze=W({name:"Table",props:Xe,setup(o){const{mergedClsPrefixRef:i,inlineThemeDisabled:b,mergedRtlRef:D}=Te(o),g=ne("Table","-table",We,Se,o,i),q=ze("Table",D,i),m=Q(()=>{const{size:C}=o,{self:{borderColor:O,tdColor:N,tdColorModal:y,tdColorPopover:R,thColor:_,thColorModal:x,thColorPopover:u,thTextColor:s,tdTextColor:h,borderRadius:$,thFontWeight:M,lineHeight:T,borderColorModal:I,borderColorPopover:U,tdColorStriped:c,tdColorStripedModal:E,tdColorStripedPopover:j,[Z("fontSize",C)]:F,[Z("tdPadding",C)]:S,[Z("thPadding",C)]:z},common:{cubicBezierEaseInOut:K}}=g.value;return{"--n-bezier":K,"--n-td-color":N,"--n-td-color-modal":y,"--n-td-color-popover":R,"--n-td-text-color":h,"--n-border-color":O,"--n-border-color-modal":I,"--n-border-color-popover":U,"--n-border-radius":$,"--n-font-size":F,"--n-th-color":_,"--n-th-color-modal":x,"--n-th-color-popover":u,"--n-th-font-weight":M,"--n-th-text-color":s,"--n-line-height":T,"--n-td-padding":S,"--n-th-padding":z,"--n-td-color-striped":c,"--n-td-color-striped-modal":E,"--n-td-color-striped-popover":j}}),l=b?Pe("table",Q(()=>o.size[0]),m,o):void 0;return{rtlEnabled:q,mergedClsPrefix:i,cssVars:b?void 0:m,themeClass:l==null?void 0:l.themeClass,onRender:l==null?void 0:l.onRender}},render(){var o;const{mergedClsPrefix:i}=this;return(o=this.onRender)===null||o===void 0||o.call(this),Ne("table",{class:[`${i}-table`,this.themeClass,{[`${i}-table--rtl`]:this.rtlEnabled,[`${i}-table--bottom-bordered`]:this.bottomBordered,[`${i}-table--bordered`]:this.bordered,[`${i}-table--single-line`]:this.singleLine,[`${i}-table--single-column`]:this.singleColumn,[`${i}-table--striped`]:this.striped}],style:this.cssVars},this.$slots)}});function Ye(o){return A({url:qe("/order",o),method:"get"})}function eo(o){return A({url:"/order",method:"post",data:o})}function oo(o,i){return A({url:"/order/"+o,method:"patch",data:i})}function re(o){return A({url:"/order/"+o,method:"delete"})}function to(o){return A({url:"/order/send?id= "+o,method:"post"})}const ro=W({name:"OrderOperateModal",__name:"order-operate-modal",props:Y({operateType:{},rowData:{}},{visible:{type:Boolean,default:!1},visibleModifiers:{}}),emits:Y(["submitted"],["update:visible"]),setup(o,{emit:i}){oe(),le();const b=o,D=i,g=ae(o,"visible"),{validate:q}=se();de();const m=Q(()=>({add:"新增",edit:"编辑"})[b.operateType]),l=p(C());function C(){return{id:null,phone:null,status:null,orderNo:null,receiveTime:null,sendTime:null}}function O(){l.value=C(),console.log("=== model.value======》",l.value),b.operateType==="edit"&&b.rowData&&Object.assign(l.value,b.rowData),l.value.url&&(x.value=[{id:"1",name:"我是react.png",status:"finished",url:l.value.url}],l.value.url)}function N(){g.value=!1}async function y(){var u;if(await q(),l.value.id){const{error:s,data:h}=await oo(l.value.id,l.value);if(s)return}else{const{error:s,data:h}=await eo(l.value);if(s)return}(u=window.$message)==null||u.success(P("common.updateSuccess")),N(),D("submitted")}Be(g,()=>{g.value?(O(),_()):x.value=[]});const R=p([]);async function _(){const{error:u,data:s}=await Ve(l.value.orderNo);R.value=s}const x=p([]);return p([{label:"待出餐",value:1},{label:"已出餐",value:2},{label:"已取餐",value:3}]),p([]),(u,s)=>{const h=Ae,$=Ze,M=ue,T=L,I=te,U=ce;return V(),ie(U,{show:g.value,"onUpdate:show":s[0]||(s[0]=c=>g.value=c),title:m.value,preset:"card",class:"w-800px"},{footer:r(()=>[e(I,{justify:"end",size:16},{default:r(()=>[e(T,{onClick:N},{default:r(()=>[B(k(a(P)("common.cancel")),1)]),_:1}),e(T,{type:"primary",onClick:y},{default:r(()=>[B(k(a(P)("common.confirm")),1)]),_:1})]),_:1})]),default:r(()=>[e(M,{class:"h-480px pr-20px"},{default:r(()=>[e($,{bordered:!1,"single-line":!1},{default:r(()=>[s[1]||(s[1]=v("thead",null,[v("tr",null,[v("th",null,"订单号"),v("th",null,"名称"),v("th",null,"封面"),v("th",null,"价格"),v("th",null,"数量"),v("th",null,"总价")])],-1)),v("tbody",null,[(V(!0),ee(De,null,Oe(R.value,c=>(V(),ee("tr",{key:c.id},[v("td",null,k(c.orderNo),1),v("td",null,k(c.skuName),1),v("td",null,[e(h,{src:c.skuImg,width:"80"},null,8,["src"])]),v("td",null,k(c.price),1),v("td",null,k(c.number),1),v("td",null,k(c.price*c.number),1)]))),128))])]),_:1})]),_:1})]),_:1},8,["show","title"])}}}),no=W({name:"OrderSearch",__name:"order-search",props:{model:{required:!0},modelModifiers:{}},emits:Y(["reset","search"],["update:model"]),setup(o,{emit:i}){oe();const b=i,{formRef:D,validate:g,restoreValidation:q}=se(),m=ae(o,"model"),l=Q(()=>{const{patternRules:x}=de();return{userEmail:x.email,userPhone:x.phone}});async function C(){await q(),b("reset")}async function O(){await g(),b("search")}const N=p([{label:"待出餐",value:1},{label:"已出餐",value:2},{label:"已取餐",value:3}]),y=p([]);async function R(){const{error:x,data:u}=await me();if(u){y.value=[];for(const s of u)y.value.push({label:s.name,value:s.id})}}function _(){R()}return _(),(x,u)=>{const s=Ie,h=Me,$=Ue,M=Ge,T=L,I=Je,U=te,c=$e,E=Re,j=He,F=Ke,S=pe;return V(),ie(S,{bordered:!1,size:"small",class:"card-wrapper"},{default:r(()=>[e(F,{"default-expanded-names":["order-search"]},{default:r(()=>[e(j,{title:"搜索",name:"order-search"},{default:r(()=>[e(E,{ref_key:"formRef",ref:D,model:m.value,rules:l.value,"label-placement":"left","label-width":80},{default:r(()=>[e(c,{responsive:"screen","item-responsive":""},{default:r(()=>[e(h,{span:"24 s:12 m:6",label:"用户：",path:"qp_userId_eq",class:"pr-24px"},{default:r(()=>[e(s,{value:m.value.qp_userId_eq,"onUpdate:value":u[0]||(u[0]=z=>m.value.qp_userId_eq=z),options:y.value,placeholder:"请选择"},null,8,["value","options"])]),_:1}),e(h,{span:"24 s:12 m:6",label:"订单状态：",path:"qp_status_eq",class:"pr-24px"},{default:r(()=>[e(s,{value:m.value.qp_status_eq,"onUpdate:value":u[1]||(u[1]=z=>m.value.qp_status_eq=z),options:N.value,placeholder:"请选择"},null,8,["value","options"])]),_:1}),e(h,{span:"24 s:12 m:6",label:"订单编号：",path:"qp_orderNo_like",class:"pr-24px"},{default:r(()=>[e($,{value:m.value.qp_orderNo_like,"onUpdate:value":u[2]||(u[2]=z=>m.value.qp_orderNo_like=z),placeholder:"请输入"},null,8,["value"])]),_:1}),e(h,{span:"24 m:6",class:"pr-24px"},{default:r(()=>[e(U,{class:"w-full",justify:"start"},{default:r(()=>[e(T,{onClick:C},{icon:r(()=>[e(M,{class:"text-icon"})]),default:r(()=>[B(" "+k(a(P)("common.reset")),1)]),_:1}),e(T,{type:"primary",ghost:"",onClick:O},{icon:r(()=>[e(I,{class:"text-icon"})]),default:r(()=>[B(" "+k(a(P)("common.search")),1)]),_:1})]),_:1})]),_:1})]),_:1})]),_:1},8,["model","rules"])]),_:1})]),_:1})]),_:1})}}}),lo={class:"min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto"};function ao(o){return typeof o=="function"||Object.prototype.toString.call(o)==="[object Object]"&&!Le(o)}const ho=W({name:"order",__name:"index",setup(o){const i=le(),{bool:b,setTrue:D}=je();oe(),p(null);const{columns:g,columnChecks:q,data:m,loading:l,pagination:C,getData:O,getDataByPage:N,searchParams:y,resetSearchParams:R}=Ee({apiFn:Ye,apiParams:{currentPage:1,pageSize:10,qp_userId_eq:null,qp_status_eq:null,qp_orderNo_like:null,qp_receiveTime_like:null,qp_sendTime_like:null},columns:()=>[{type:"selection",align:"center",width:48},{key:"index",title:"序号",align:"center"},{key:"orderNo",title:"订单编号",align:"center"},{key:"userId",title:"用户",align:"center",render:t=>{if(t.userId){let n=[];for(const f of j.value)f.value==t.userId&&n.push(f.label);return n.join("、")}return""}},{key:"status",title:"订单状态",align:"center",render:t=>{if(t.status){let n=[];for(const f of E.value)f.value===t.status&&n.push(f.label);return n.join("、")}return""}},{key:"sendTime",title:"出餐时间",width:154,align:"center",render:t=>t.sendTime?t.sendTime.replace("T"," "):""},{key:"receiveTime",title:"取餐时间",width:154,align:"center",render:t=>t.receiveTime?t.receiveTime.replace("T"," "):""},{key:"createTime",title:"下单时间",width:154,align:"center",render:t=>t.createTime?t.createTime.replace("T"," "):""},{key:"operate",title:P("common.operate"),align:"center",width:230,render:t=>e("div",{class:"flex-center justify-center gap-8px"},[e(L,{type:"warning",ghost:!0,size:"small",onClick:()=>{S.value=!0,z.value=t.id,K.value=t.status}},{default:()=>[B("发货")]}),e(L,{type:"primary",ghost:!0,size:"small",onClick:()=>I(t)},{default:()=>[B("订单详情")]}),e(xe,{onPositiveClick:()=>M(t.id)},{default:()=>P("common.confirmDelete"),trigger:()=>{let n;return e(L,{type:"error",ghost:!0,size:"small"},ao(n=P("common.delete"))?n:{default:()=>[n]})}})])}]}),{checkedRowKeys:_,onBatchDeleted:x,onDeleted:u}=Fe(m,O),s=p("add");function h(){s.value="add",D()}async function $(){const{error:t,data:n}=await re(_.value);console.log(_.value),x()}async function M(t){const{error:n,data:f}=await re(t);console.log(t),u()}const T=p(null);function I(t){s.value="edit",T.value={...t},D()}const U=p([]);function c(){F()}const E=p([{label:"待出餐",value:1},{label:"已出餐",value:2},{label:"已取餐",value:3}]),j=p([]);async function F(){const{error:t,data:n}=await me();if(n){j.value=[];for(const f of n)j.value.push({label:f.name,value:f.id})}}const S=p(!1),z=p(null),K=p(null);async function ve(){var f,H;if(K.value!==1){(f=window.$message)==null||f.success("订单已发货，无需再次发货"),S.value=!1;return}const{error:t,data:n}=await to(z.value);t||(S.value=!1,(H=window.$message)==null||H.success("发货成功"),N())}async function be(){S.value=!1}return c(),(t,n)=>{const f=ye,H=Qe,fe=pe,_e=ue,he=te,ge=ce;return V(),ee("div",lo,[e(no,{model:a(y),"onUpdate:model":n[0]||(n[0]=w=>J(y)?y.value=w:null),onReset:a(R),onSearch:a(N)},null,8,["model","onReset","onSearch"]),e(fe,{title:"数据列表",bordered:!1,size:"small",class:"sm:flex-1-hidden card-wrapper"},{"header-extra":r(()=>[e(f,{columns:a(q),"onUpdate:columns":n[1]||(n[1]=w=>J(q)?q.value=w:null),"disabled-delete":a(_).length===0,loading:a(l),hideAdd:!0,onAdd:h,onDelete:$,onRefresh:a(O)},null,8,["columns","disabled-delete","loading","onRefresh"])]),default:r(()=>[e(H,{"checked-row-keys":a(_),"onUpdate:checkedRowKeys":n[2]||(n[2]=w=>J(_)?_.value=w:null),columns:a(g),data:a(m),size:"small","flex-height":!a(i).isMobile,"scroll-x":1088,loading:a(l),"row-key":w=>w.id,remote:"",pagination:a(C),class:"sm:h-full"},null,8,["checked-row-keys","columns","data","flex-height","loading","row-key","pagination"]),e(ro,{visible:a(b),"onUpdate:visible":n[3]||(n[3]=w=>J(b)?b.value=w:null),"operate-type":s.value,"row-data":T.value,"all-pages":U.value,onSubmitted:a(N)},null,8,["visible","operate-type","row-data","all-pages","onSubmitted"])]),_:1}),e(ge,{show:S.value,"onUpdate:show":n[4]||(n[4]=w=>S.value=w),title:"订单发货",preset:"card",class:"w-800px"},{footer:r(()=>[e(he,{justify:"end",size:16},{default:r(()=>[e(a(L),{onClick:be},{default:r(()=>[B(k(a(P)("common.cancel")),1)]),_:1}),e(a(L),{type:"primary",onClick:ve},{default:r(()=>[B(k(a(P)("common.confirm")),1)]),_:1})]),_:1})]),default:r(()=>[e(_e,{class:"h-50px pr-20px"},{default:r(()=>n[5]||(n[5]=[B(" 是否确定发货 ")])),_:1})]),_:1},8,["show"])])}}});export{ho as default};
