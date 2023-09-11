let baseUrl ="http://localhost:8081/posApp/";

getAllCustomers();

$("#customerGetAll-btn").click(function (){
    getAllCustomers();
});

function getAllCustomers() {
    $("#tblCustomer").empty();
    $.ajax({
        url: baseUrl + "customer",
        method: "GET",
        success: function (cus) {
            for (let i = 0; i < cus.length; i++) {
                let id = cus[i].id;
                let name = cus[i].name;
                let address = cus[i].address;
                let salary = cus[i].salary;
                let row = `<tr><td>${id}</td><td>${name}</td><td>${address}</td><td>${salary}</td>`;
                $("#tblCustomer").append(row);
            }
        }

    });
}

$("#customerSave-btn").click(function () {
    let formData = $("#CustomerForm").serialize();
    let customer = new Customer($("#txtCustID").val(), $("#txtCustName").val(), $("#txtCustAddress").val(), $("#txtCustSalary").val());

    let json = {
        id: customer.getCusId(),
        name: customer.getCusName(),
        address: customer.getCusAddress(),
        salary: customer.getCusSalary()
    };

    if ($(this).text() == "Save") {
        $.ajax({
            url: baseUrl + "customer",
            type: "POST",
            data: formData,
            dataType: "json",
            success: function (res) {
                getAllCustomers();
                alert(res.message);
            },
            error: function (error) {
                let parse = JSON.parse(error.responseText);
                alert(parse.message);
            }
        });

    }

});


//delete customer
$("#customerDelete-btn").click(function () {
    let id = $("#txtCustID").val();
    $.ajax({
        url: baseUrl+"customer?cusID=" + id,
        method: "delete",
        success: function (resp) {
            getAllCustomers();
            alert(resp.message);
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});

//update customer
$("#customerUpdated-btn").click(function () {
    let cusId = $('#txtCustID').val();
    let cusName = $('#txtCustName').val();
    let cusAddress = $('#txtCustAddress').val();
    let cusSalary = $('#txtCustSalary').val();
    var customerOb = {
        cusID: cusId,
        cusName: cusName,
        cusAddress: cusAddress,
        cusSalary: cusSalary
    }
    $.ajax({
        url: baseUrl+"customer",
        method: "put",
        contentType: "application/json",
        data: JSON.stringify(customerOb),
        dataType: "json",
        success: function (resp) {
            getAllCustomers();
            alert(resp.message);
        },
        error: function (error) {
            let message = JSON.parse(error.responseText).message;
            alert(message);
        }
    });
});
