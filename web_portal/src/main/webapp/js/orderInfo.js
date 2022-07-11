new Vue({
    el: "#app",
    data: {
        addressList: [],//地址列表
        address: {},//当前地址
        cartList: [],//购物车列表
        totalValue: {
            totalNum: 0,
            totalMoney: 0
        }, //总价
        order: {},
        user: ''
    },
    methods: {
        loadAddress: function (categoryId) {
            let _this = this;
            axios.post("/address/findListByLoginUser.do")
                .then(function (response) {
                    _this.addressList = response.data;
                    for (var i = 0; i < _this.addressList.length; i++) {
                        if (_this.addressList[i].isDefault === '1') {
                            _this.address = _this.addressList[i];
                            break;
                        }
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        isSeletedAddress: function (address) {
            if (address === this.address) {
                return true;
            } else {
                return false;
            }
        },
        selectAddress: function (address) {
            this.address = address;
            console.log(address);
        },
        selectPayType: function (type) {
            this.order.paymentType = type;
        },
        loadCartData: function (categoryId) {
            let _this = this;
            axios.post("/cart/findCartList.do")
                .then(function (response) {
                    _this.cartList = response.data;
                    _this.totalValue = _this.sum(_this.cartList);
                    console.log(_this.totalValue);
                    console.log(_this.cartList);
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        sum: function (cartList) {
            var totalValue = {totalNum: 0, totalMoney: 0};
            for (var i = 0; i < cartList.length; i++) {
                var cart = cartList[i];//购物车对象
                for (var j = 0; j < cart.orderItemList.length; j++) {
                    var orderItem = cart.orderItemList[j];//购物车明细
                    totalValue.totalNum += orderItem.num;//累加数量
                    totalValue.totalMoney += orderItem.price;//累加金额
                }
            }
            return totalValue;
        },
        getUserInfo: function () {
            let _this = this;
            axios.get("/user/getUserInfo.do").then((res) => {
                let data = res.data;
                _this.user = data;
            })
        },
    },
    created: function () {
        this.getUserInfo();
    },
})