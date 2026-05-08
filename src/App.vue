<script setup>
import { dashboardData } from "./data/dashboardData.js";
import Header from "./components/layout/Header.vue";
import DashboardPage from "./pages/DashboardPage.vue";
</script>

<template>
  <a class="skip-link" href="#main">본문 바로가기</a>
  <Header
    :navigation="dashboardData.navigation"
    :profile="dashboardData.profile"
    :service-links="dashboardData.serviceLinks"
  />
  <DashboardPage :dashboard-data="dashboardData" />
</template>
