<script setup>
import Logo from "./Logo.vue";
import PrimaryNav from "./PrimaryNav.vue";
import UserTools from "./UserTools.vue";
import ServiceLinks from "./ServiceLinks.vue";
import MobileMenu from "./MobileMenu.vue";

defineProps({
  navigation: {
    type: Array,
    required: true
  },
  profile: {
    type: Object,
    required: true
  },
  serviceLinks: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <header class="global-header">
    <div class="header-inner">
      <Logo />
      <PrimaryNav :navigation="navigation" />
      <UserTools :profile="profile" />
      <ServiceLinks :service-links="serviceLinks" />
      <MobileMenu :navigation="navigation" :service-links="serviceLinks" />
    </div>
  </header>
</template>
