import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router'
import Index from '../views/user/Index.vue'

const routes = [
  { 
    path: '/', 
    component: Index,
    children: [
      {
        path: '/',
        name: 'Main',
        component: () => import('@/views/user/Main.vue')
      },
      {
        path: '/message',
        name: 'Message',
        component: () => import('@/views/user/Message.vue')
      },
      {
        path: '/guide',
        name: 'Guide',
        component: () => import('@/views/user/Guide.vue')
      },
      {
        path: '/announcement/detail/:id',
        name: 'AnnouncementDetail',
        component: () => import('@/views/user/AnnouncementDetail.vue')
      },
      {
        path: '/course',
        name: 'Course',
        component: () => import('@/views/user/Course.vue')
      },
      {
        path: '/courseReserve',
        name: 'CourseReserve',
        component: () => import('@/views/user/CourseReserve.vue')
      },
      {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/user/Register.vue')
      },
      {
        path: '/login',
        name: 'UserLogin',
        component: () => import('@/views/user/UserLogin.vue')
      },
      
      {
        path: '/userInfo',
        name: 'UserInfo',
        component: () => import('@/views/user/UserInfo.vue')
      },
      {
        path: '/skus',
        name: 'Skus',
        component: () => import('@/views/user/Skus.vue')
      },
      {
        path: '/sku/detail/:id',
        name: 'SkuDetail',
        component: () => import('@/views/user/SkuDetail.vue')
      },
      {
        path: '/course/detail/:id',
        name: 'CourseDetail',
        component: () => import('@/views/user/CourseDetail.vue')
      },
      {
        path: '/courses',
        name: 'Courses',
        component: () => import('@/views/user/Courses.vue')
      },
      {
        path: '/coaches',
        name: 'Coaches',
        component: () => import('@/views/user/Coaches.vue')
      },
      {
        path: '/shoppingCar',
        name: 'ShoppingCar',
        component: () => import('@/views/user/ShoppingCar.vue')
      },
      {
        path: '/orders',
        name: 'Orders',
        component: () => import('@/views/user/Orders.vue')
      },
      {
        path: '/post',
        name: 'PostIndex',
        component: () => import('@/views/user/PostIndex.vue')
      },
      {
        path: '/postDetail/:id',
        name: 'PostDetail',
        component: () => import('@/views/user/PostDetail.vue')
      },
      {
        path: '/postAdd',
        name: 'UserPostAdd',
        component: () => import('@/views/user/UserPostAdd.vue')
      },
      {
        path: '/postUpdate/:id',
        name: 'UserPostUpdate',
        component: () => import('@/views/user/UserPostUpdate.vue')
      },
    ]
  },
]



const router = createRouter({
  mode: 'history', // 去掉url中的#
  history: createWebHistory(process.env.BASE_URL),
  // history: createWebHashHistory(process.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { left: 0, top: 0 }
    }
  }
})

router.beforeEach((to, from, next) => {
  console.log('to', to)
  if(localStorage.getItem('adminToken') && (to.path=='/admin' || to.path == '/admin/login')) {
    next({path: '/admin/home'})
  }
  next()
})

export default router
