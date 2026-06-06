<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-brand">
          <h3 class="footer-logo">极简衣橱</h3>
          <p class="footer-desc">简约设计，极致品质</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>购物指南</h4>
            <ul>
              <li><a href="#">商品分类</a></li>
              <li><a href="#">新品上市</a></li>
              <li><a href="#">热销榜单</a></li>
            </ul>
          </div>
          <div class="link-group">
            <h4>客户服务</h4>
            <ul>
              <li><a href="#">配送说明</a></li>
              <li><a href="#">退换货政策</a></li>
              <li><a href="#">联系我们</a></li>
            </ul>
          </div>
          <div class="link-group">
            <h4>关于我们</h4>
            <ul>
              <li><a href="#">品牌故事</a></li>
              <li><a href="#">加入我们</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 极简衣橱. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: var(--color-card);
  border-top: 1px solid var(--color-divider);
  padding: var(--spacing-12) 0 var(--spacing-6);
}

.footer-content {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: var(--spacing-12);
  margin-bottom: var(--spacing-8);
}

.footer-logo {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-primary);
  margin-bottom: var(--spacing-3);
}

.footer-desc {
  color: var(--color-text-secondary);
  font-size: var(--text-sm);
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-8);
}

.link-group h4 {
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  margin-bottom: var(--spacing-4);
  color: var(--color-text);
}

.link-group ul {
  list-style: none;
}

.link-group li {
  margin-bottom: var(--spacing-2);
}

.link-group a {
  color: var(--color-text-secondary);
  font-size: var(--text-sm);
  transition: color 0.2s ease;
}

.link-group a:hover {
  color: var(--color-primary);
}

.footer-bottom {
  text-align: center;
  padding-top: var(--spacing-6);
  border-top: 1px solid var(--color-divider);
  color: var(--color-text-placeholder);
  font-size: var(--text-sm);
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: var(--spacing-8);
  }
  .footer-links {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>