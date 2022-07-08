new Vue({
    el: "#app",
    data: {
        cartList: [],//购物车列表
        totalItem: 0,
        totalMoney: 0,
        selectedId: [],
    },
    methods: {
        loadCartData: function () {
            let _this = this;
            axios.post("/cart/findCartList.do")
                .then(function (response) {
                    _this.cartList = response.data;
                    //计算一共有多少个商品
                    let sum = 0;
                    for (let i = 0; i < _this.cartList.length; i++) {
                        sum += _this.cartList[i].orderItemList.length;
                    }
                    _this.totalItem = sum;
                    console.log(_this.cartList);
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        handleSelected: function (event, id) {
            if (event.target.checked) {
                //选中
                this.selectedId.push(id);
            } else {
                //取消选中
                let idx = this.selectedId.indexOf(id);
                this.selectedId.splice(idx, 1)
            }
            // console.log(this.selectedId);
        },
        chooseAll: function (event) {
            let sellerInp = document.getElementsByName('sellerCheckbox');
            let inp = document.getElementsByClassName('itemCheckbox');
            if (event.target.checked) {
                //将商家选择标签也选中
                for (let i = 0; i < sellerInp.length; i++) {
                    sellerInp[i].checked = true;
                }
                //全选
                for (let i = 0; i < inp.length; i++) {
                    inp[i].checked = true;
                }
                for (let i = 0; i < this.cartList.length; i++) {
                    for (let j = 0; j < this.cartList[i].orderItemList.length; j++) {
                        let idx = this.selectedId.indexOf(this.cartList[i].orderItemList[j].itemId);
                        if (idx != -1) {
                            //如果已经存在了,就不需要再添加
                            continue;
                        }
                        this.selectedId.push(this.cartList[i].orderItemList[j].itemId);
                    }
                }
            } else {
                //将商家选择标签取消选中
                for (let i = 0; i < sellerInp.length; i++) {
                    sellerInp[i].checked = false;
                }
                //取消选中
                for (let i = 0; i < inp.length; i++) {
                    inp[i].checked = false;
                }
                for (let i = 0; i < this.cartList.length; i++) {
                    for (let j = 0; j < this.cartList[i].orderItemList.length; j++) {
                        let idx = this.selectedId.indexOf(this.cartList[i].orderItemList[j].itemId);
                        this.selectedId.splice(idx, 1);
                    }
                }
            }
            // console.log(this.selectedId)
        },
        chooseOneSellerAll: function (event, sellerName) {
            let inp = document.getElementsByName(sellerName);
            if (event.target.checked) {
                //全选
                for (let i = 0; i < inp.length; i++) {
                    inp[i].checked = true;
                }
                for (let i = 0; i < this.cartList.length; i++) {
                    if (this.cartList[i].sellerName === sellerName) {
                        for (let j = 0; j < this.cartList[i].orderItemList.length; j++) {
                            let idx = this.selectedId.indexOf(this.cartList[i].orderItemList[j].itemId);
                            if (idx != -1) {
                                //如果已经存在了,就不需要再添加
                                continue;
                            }
                            this.selectedId.push(this.cartList[i].orderItemList[j].itemId);
                        }
                    }
                }
            } else {
                //取消选中
                for (let i = 0; i < inp.length; i++) {
                    inp[i].checked = false;
                }
                for (let i = 0; i < this.cartList.length; i++) {
                    if (this.cartList[i].sellerName === sellerName) {
                        for (let j = 0; j < this.cartList[i].orderItemList.length; j++) {
                            let idx = this.selectedId.indexOf(this.cartList[i].orderItemList[j].itemId);
                            if (idx === -1) {
                                //如果没有找到这个id,就不需要删除
                                continue;
                            }
                            this.selectedId.splice(idx, 1);
                        }
                    }
                }
            }
            // console.log(this.selectedId)
        },
        //商品数量加减
        addNum: function (sellerId, itemId, x) {
            for (let i = 0; i < this.cartList.length; i++) {
                if (this.cartList[i].sellerId === sellerId) {
                    for (let j = 0; j < this.cartList[i].orderItemList.length; j++) {
                        if (this.cartList[i].orderItemList[j].itemId === itemId) {
                            if (this.cartList[i].orderItemList[j].num === 1 && x === -1) {
                                this.cartList[i].orderItemList[j].num = 1;
                            } else {
                                // console.log(this.cartList[i].orderItemList[j].num)
                                this.cartList[i].orderItemList[j].num += x;
                            }
                        }
                    }
                }
            }
            //处理完商品数量要重新计算价格
            this.sum();
        },
        //计算购物车总价
        sum: function () {
            let totalMoney = 0;
            for (let i = 0; i < this.cartList.length; i++) {
                for (let j = 0; j < this.cartList[i].orderItemList.length; j++) {
                    let idx = this.selectedId.indexOf(this.cartList[i].orderItemList[j].itemId);
                    if (idx != -1) {
                        //如果找到这个id,就计算价格
                        totalMoney += this.cartList[i].orderItemList[j].num * this.cartList[i].orderItemList[j].price;
                    }
                }
            }
            this.totalMoney = totalMoney;
        },
        changeItemNum: function (sellerId, itemId) {
            for (let i = 0; i < this.cartList.length; i++) {
                if (this.cartList[i].sellerId === sellerId) {
                    for (let j = 0; j < this.cartList[i].orderItemList.length; j++) {
                        if (this.cartList[i].orderItemList[j].itemId === itemId) {
                            this.cartList[i].orderItemList[j].num = parseInt(this.cartList[i].orderItemList[j].num);
                        }
                        if (this.cartList[i].orderItemList[j].num <= 0) {
                            this.cartList[i].orderItemList[j].num = 1;
                        }
                    }
                }
            }
            this.sum()
        }
    },
    created: function () {//创建对象时调用
        this.loadCartData()
    },
    watch: {
        'selectedId.length': {
            handler(newValue, oldValue) {
                if (newValue !== oldValue) {
                    // 操作
                    let inp = document.getElementsByName('allCheckbox');
                    if (this.selectedId.length === 0) {
                        inp[0].checked = false;
                    } else if (this.selectedId.length === this.totalItem) {
                        inp[0].checked = true;
                    }
                    //重新计算总价
                    this.sum();
                }
            }
        },

    }
})