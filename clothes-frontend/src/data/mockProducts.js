// 使用 Unsplash 免费高质量时尚图片
export const mockProducts = [
  {
    id: 1,
    name: '极简纯色衬衫',
    price: 399,
    typeId: 2,
    typeName: '上衣',
    description: '采用优质棉质面料，柔软亲肤，简约设计，百搭款式。经典款式，适合各种场合穿着。',
    tags: ['夏季', '简约'],
    image: 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 2,
    name: '经典直筒裤',
    price: 459,
    typeId: 3,
    typeName: '裤子',
    description: '修身直筒版型，高腰设计拉长腿部线条，优质面料垂坠感好。',
    tags: ['经典', '通勤'],
    image: 'https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 3,
    name: '优雅连衣裙',
    price: 699,
    typeId: 1,
    typeName: '连衣裙',
    description: '法式优雅风格，收腰设计展现女性曲线，裙摆飘逸。',
    tags: ['优雅', '约会'],
    image: 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L']
  },
  {
    id: 4,
    name: '质感风衣',
    price: 1299,
    typeId: 4,
    typeName: '外套',
    description: '经典 trench coat 版型，防风防水面料，春秋必备单品。',
    tags: ['经典', '防风'],
    image: 'https://images.unsplash.com/photo-1544022613-e87ca75a784a?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 5,
    name: '休闲T恤',
    price: 199,
    typeId: 2,
    typeName: '上衣',
    description: '100%有机棉，透气舒适，基础百搭款。',
    tags: ['基础', '舒适'],
    image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL', 'XXL']
  },
  {
    id: 6,
    name: '高腰阔腿裤',
    price: 399,
    typeId: 3,
    typeName: '裤子',
    description: '高腰设计显瘦显高，阔腿版型舒适有型，通勤休闲两相宜。',
    tags: ['显瘦', '通勤'],
    image: 'https://images.unsplash.com/photo-1509631179647-0177331693ae?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 7,
    name: '碎花连衣裙',
    price: 599,
    typeId: 1,
    typeName: '连衣裙',
    description: '清新碎花图案，V领设计修饰脸型，夏日浪漫首选。',
    tags: ['碎花', '浪漫'],
    image: 'https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L']
  },
  {
    id: 8,
    name: '牛仔外套',
    price: 499,
    typeId: 4,
    typeName: '外套',
    description: '经典牛仔蓝，宽松版型，百搭不过时。',
    tags: ['经典', '百搭'],
    image: 'https://images.unsplash.com/photo-1523205771623-e0faa4d2813d?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 9,
    name: '针织开衫',
    price: 359,
    typeId: 2,
    typeName: '上衣',
    description: '柔软针织面料，温暖舒适，春秋季节必备。',
    tags: ['温暖', '舒适'],
    image: 'https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 10,
    name: '半身裙',
    price: 329,
    typeId: 3,
    typeName: '裤子',
    description: 'A字版型显瘦，中长款优雅大方，适合多种场合。',
    tags: ['优雅', '显瘦'],
    image: 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L']
  },
  {
    id: 11,
    name: '羊毛大衣',
    price: 1599,
    typeId: 4,
    typeName: '外套',
    description: '高品质羊毛面料，保暖性佳，经典双排扣设计。',
    tags: ['保暖', '经典'],
    image: 'https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L', 'XL']
  },
  {
    id: 12,
    name: '丝绸衬衫',
    price: 599,
    typeId: 2,
    typeName: '上衣',
    description: '真丝面料，光泽柔和，触感丝滑，高级感满满。',
    tags: ['高级', '光泽'],
    image: 'https://images.unsplash.com/photo-1564257631407-4deb1f99d992?w=400&h=533&fit=crop',
    sizes: ['S', 'M', 'L']
  }
]

export const productTypes = [
  { id: 0, name: '全部' },
  { id: 1, name: '连衣裙' },
  { id: 2, name: '上衣' },
  { id: 3, name: '裤子' },
  { id: 4, name: '外套' }
]