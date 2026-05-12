<template>
  <footer class="app-footer">
    <div class="app-footer-inner">
      <div class="footer-logo">SSAFY EDU</div>
      <div class="footer-copy">
        <nav class="footer-links" aria-label="Footer links">
          <RouterLink to="/403">이용약관</RouterLink>
          <span aria-hidden="true">|</span>
          <RouterLink to="/403">개인정보처리방침</RouterLink>
        </nav>
        <p>본 사이트의 콘텐츠는 저작권법의 보호를 받는 바 무단 전재, 복사, 배포 등을 금합니다.</p>
        <small>Copyright © SAMSUNG All Rights Reserved.</small>
      </div>
      <RouterLink class="footer-ebook" to="/403">e-book</RouterLink>
    </div>
  </footer>
</template>
