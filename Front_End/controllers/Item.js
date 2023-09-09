

getAllItems();

$("#itemGetAll-btn").click(function (){
    getAllItems();
}) ;

function getAllItems(){
    $("#tblItem").empty();
    $.ajax({
        url: baseUrl + "item?option=getAll",
        method : "GET",
        success:function (data){
            for (let i = 0; i < data.length; i++){
                let code = data[i].code;
                let  name = data[i].name;
                let price = data[i].Price;
                let qty = data[i].Qty;
                let row = `<tr><td>${code}</td><td>${name}</td><td>${price}</td><td>${qty}</td>`;
                $("#tblItem").append(row);

            }
    },
    });
}
// add Item
$("#itemSave-btn").click(function () {
    let formData = $("#item-form px-2").serialize();
    $.ajax({
        url: baseUrl+"item",
        method: "post",
        data: formData,
        dataType: "json",
        success: function (res) {
            console.log("success Method Invoked");
            console.log(res);
            alert(res.message);
            getAllItems();
        },
        error: function (error) {
            console.log("Error Method Invoked");
            console.log(JSON.parse(error.responseText));
            alert(JSON.parse(error.responseText).message);
        }
    });
});

//delete Item
$("#itemDelete-btn").click(function () {
    let code = $("#txtItemCode").val();
    $.ajax({
        url: baseUrl+"item?code=" + code,
        method: "delete",
        success: function (resp) {
            getAllItems();
            alert(resp.message);
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});

//update Item
$("#itemUpdated-btn").click(function () {
    let code = $('#txtItemCode').val();
    let description = $('#txtItemName').val();
    let qtyOnHand = $('#txtItemQTY').val();
    let unitPrice = $('#txtItemPrice').val();
    var ItemOb = {
        code: code,
        description: description,
        qtyOnHand: qtyOnHand,
        unitPrice: unitPrice
    }
    $.ajax({
        url: baseUrl+"item",
        method: "put",
        contentType: "application/json",
        data: JSON.stringify(ItemOb),
        dataType: "json",
        success: function (resp) {
            getAllItems();
            alert(resp.message);
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});
