<template>
  <div class="section">
    <div v-if="!isAuthenticated">
      <a class="btn is-info" href="/api/login">Login</a>
    </div>
    <div v-else>
      <a class="btn is-info" href="/api/logout">Logout</a>
    </div>
    <h2>{{message}}</h2>
    <h2>Access Token: {{accessToken}}</h2>
    <h2>ID Token: {{idToken}}</h2>
    <h2>Is Authenticated: {{isAuthenticated}}</h2>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'HelloWorld',
  mounted () {
    this.getUser()
  },
  data () {
    return {
      isAuthenticated: false,
      accessToken: '',
      idToken: '',
      message: 'Loading....'
    }
  },
  methods: {
    login: function () {
      axios.get('api/login')
    },
    logout: function () {
      axios.get('api/logout')
        .then(response => {
          this.isAuthenticated = false
          this.getUser()
        })
    },
    getUser: function () {
      axios.get('/api/user')
        .then(response => {
          this.accessToken = response.data.accessToken
          this.idToken = response.data.idToken
          if (this.accessToken && this.idToken) {
            this.isAuthenticated = true
          }
          this.message = 'Loaded'
        })
        .catch(() => {
          this.message = 'Error loading.'
        })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
