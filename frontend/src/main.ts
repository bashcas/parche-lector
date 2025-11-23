import { createApp } from 'vue'
import { VueQueryPlugin } from '@tanstack/vue-query'
import App from './App.vue'
import router from './router'
import { queryClient } from './shared/config/queryClient'
import './style.css'

const app = createApp(App)

app.use(router)
app.use(VueQueryPlugin, { queryClient })

app.mount('#app')
