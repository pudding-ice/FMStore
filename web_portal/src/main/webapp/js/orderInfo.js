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
        order: {}
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

    },
    created: {}
})