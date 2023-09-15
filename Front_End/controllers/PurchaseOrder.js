
var genarateOrderId = 0;


$("#orderId").val('0-00' + (genarateOrderId += 1));
// Call the function to start loading customer IDs
loadCustomerIds();
loadItemIds();
function loadCustomerIds() {
    $("#custIDCMB").empty();
    $("#custIDCMB").append($("<option></option>").attr("value", 0).text("Select ID"));

    var countCustomerId = 1;

    $.ajax({
        url: baseUrl+ "customer?option=GetAll",
        method: "GET",
        success: function (resp) {
            for (let ids of resp) {
                $("#custIDCMB").append($("<option></option>").attr("value", countCustomerId).text(ids.id));
                countCustomerId++;
                console.log(countCustomerId);
            }

            // Bind the event after options are loaded
            $("#custIDCMB").click(function () {
                loadCustomer();
            });
        },
        error: function (ob, statusText, error) {
        }
    });
}
function loadCustomer() {
    $.ajax({
        url:baseUrl+ "customer?option=search&cusID=" + $("#custIDCMB option:selected").text(),
        method: "GET",
        success: function (response) {
            $("#orderCustomerID").val(response.id);
            $("#txtcusName").val(response.name);
            $("#txtcusAddress").val(response.address);
            $("#txtcusSalary").val(response.salary);
        },
        error: function (ob, statusText, error) {
        }
    });
}

function loadItemIds() {
    $("#itemCodeCMB").empty();
    $("#itemCodeCMB").append($("<option></option>").attr("value", 0).text("Select ID"));

    var countItemId = 1;

    $.ajax({
        url: baseUrl+"item?option=getAll",
        method: "GET",
        success: function (resp) {
            for (let code of resp) {
                $("#itemCodeCMB").append($("<option></option>").attr("value", countItemId).text(code.code));
                countItemId++;
                console.log(countItemId);
            }
            // Bind the event after options are loaded
            $("#itemCodeCMB").click(function () {
                loadItems();

            });
        },
        error: function (ob, statusText, error) {
        }
    });

}

function loadItems() {
    $.ajax({
        url:baseUrl+ "item?option=search&ItemCode=" + $("#itemCodeCMB option:selected").text(),
        method: "GET",
        success: function (resp) {
            $("#itID").val(resp.code);
            $("#itName").val(resp.description);
            $("#itPrice").val(resp.unitPrice);
            $("#itQty").val(resp.qty);
            // console.log(resp);
        },
        error: function (ob, statusText, error) {
        }
    });
}




$("#btnAddToCart").click(function () {
    let itemCode = $("#itID").val();
    let itemDescription = $("#itName").val();
    let itemPrice = $("#itPrice").val();
    let itemQty = $("#itBuyQty").val();

    let cartItemsRow = [];

    let total = itemPrice * itemQty;

    cartItemsRow.push(itemCode, itemDescription, itemPrice, itemQty, total);

    let text = "Successfully Added..!";

    if (confirm(text) == true) {
        let row = `<tr>
                <td>${itemCode}</td>
                <td>${itemDescription}</td>
                <td>${itemPrice}</td>
                <td>${itemQty}</td>
                <td>${total}</td>
            </tr>`;

        $("#tblCart").append(row);
    }

    calculateTotal();


    $("#itID").val("");
    $("#itName").val("");
    $("#itPrice").val("");
    $("#itQty").val("");
   // $("#itBuyQty").val("");

    $("#orderCustomerID").focus();

    $("#orderCustomerID").val("");
    $("#txtcusName").val("");
    $("#txtcusSalary").val("");
    $("#txtcusAddress").val("");


    $("#itID").focus();

    updateQtyOnHandRow(itemCode, itemQty);

});
function calculateTotal(){
    let total=0;
    $("#tblCart tr").each(function (){
        let price=$(this).find("td:eq(4)").text();
        total+=parseFloat(price);
    });
    $("#total").val(total);
    $("#subtotal").val(total);


    $("#txtCash").on('keyup',function (){
        let cash=$("#txtCash").val();
        let subTotal=$("#subtotal").val();
        let balance=cash-subTotal;
        $("#balance").val(balance);
    });
    $("#txtDiscount").on('keyup', function () {
        var cash = $("#txtCash").val();
        var total = $("#total").text();
        var discount = $("#txtDiscount").val();
        var balance = cash - (total - discount);
        $("#balance").val(balance);

        $("#subtotal").text(total - discount);
    });

}

function updateQtyOnHandRow(code, sellQty) {
    $.ajax({
        url: baseUrl + "item?option=search&ItemCode=" + code,
        method: "GET",
        success: function (resp) {
            $("#itQty").val(resp.qty - sellQty);
        },
        error: function (ob, statusText, error) {
            console.error(error);
        }
    });
}


let cartItemsRow=[];
$("#btnPlaceOrder").click(function () {
    let orderId = $("#orderId").val();
    let orderDate = $("#txtDate").val();
    let customerId = $("#custIDCMB option:selected").val(); // Get selected customer ID
    let itemCode = $("#itemCodeCMB option:selected").val(); // Get selected item code
    let qty = $("#itBuyQty").val();
    let unitPrice = $("#total").val();

    // Create an object for the order
    let order = {
        orderId: orderId,
        date: orderDate,
        customer_ID: customerId,
        ItemCode: itemCode,
        qty: qty,
        unitPrice: unitPrice,
    };
    console.log(JSON.stringify(order));

    $.ajax({
        url: baseUrl + "placeOrder",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(order),
        setRequestHeader:"Access-Control-Allow-Origin",
        success: function (resp) {
            updateQtyOnHandRow();
            if (resp && resp.status === 200) {
                alert("Order has been saved successfully");
            } else {
                alert("Failed to save the order");
            }
        },
        error: function (ob, statusText, error) {
            alert(error);
        }
    });
});