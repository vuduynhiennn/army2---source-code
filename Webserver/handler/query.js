var format = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;
var formatMoney = new Intl.NumberFormat('vi-VN');
$.ajaxSetup({
  contentType: "application/x-www-form-urlencoded; charset=utf-8"
});

function onlyOne(checkbox) {
  var checkboxes = document.getElementsByName(checkbox.name);
  checkboxes.forEach((item) => {
    if (item !== checkbox) {
      item.checked = false
    }
  });
}

function equip(type) {
  var content = document.getElementsByClassName("content")[0];;
  var input = content.getElementsByTagName("input");
  var id = [];
  for (var i = 0; i < input.length; i++) {
    if (input[i].type == "checkbox" && input[i].checked) {
      id.push(input[i].id);
    }
  }
  if (id.length == 0) {
    $("#message").html('<span class="invalid-feedback" role="alert" style="color:red"><strong>Vui lòng chọn trang bị!</strong></span><br />');
  } else {
    $("#message").html('<span class="invalid-feedback" role="alert" style="color:blue"><strong>Đang xử lý!</strong></span><br />');
    $.post("/handler/update.php", {
        type: type,
        index: id
      },
      function(data) {
        var json = JSON.parse(data);
        var status = json.status;
        var message = json.message;
        if (status) {
          if (type == 'mac') {
            $("#message").html('<span class="invalid-feedback" role="alert" style="color:green"><strong>' + message + '</strong></span><br />');
          } else {
            $("#message").html('<span class="invalid-feedback" role="alert" style="color:green"><strong>' + message + '</strong></span><br />');
          }
          var index = JSON.parse(json.data);
          var fonts = content.getElementsByTagName("font");
          for (var a = 0; a < fonts.length; a++) {
            fonts[a].style.color = "red";
            fonts[a].innerText = "[chưa]";
          }
          for (var i = 0; i < index.length; i++) {
            if (index[i] == -1) {
              continue;
            }
            for (var a = 0; a < fonts.length; a++) {
              if (index[i] == fonts[a].id) {
                fonts[a].style.color = "green";
                fonts[a].innerText = "[mặc]";
              }
            }
          }
        } else {
          $("#message").html('<span class="invalid-feedback" role="alert" style="color:red"><strong>' + message + '</strong></span><br />');
        }
      });
  }
}

function addpoint() {
  for (var i = 0; i < 5; i++) {
    var c = $('#addp' + i).val();
    if (c == null || c == '' || c < 0 || format.test(c)) {
      $('#message').html('<span class="invalid-feedback" role="alert" style="color:red"><strong>Vui lòng nhập điểm hợp lệ</strong></span><br>');
      return;
    }
    if (c > 1000) {
      $('#message').html('<span class="badge badge-danger"><font size="2">Số điểm cộng tối đa là 1000!</font></span>');
      return;
    }
  }
  $('#message').html('<span class="invalid-feedback" role="alert" style="color:blue"><strong>Đang xử lý</strong></span><br>');
  var add0 = $('#addp0').val();
  var add1 = $('#addp1').val();
  var add2 = $('#addp2').val();
  var add3 = $('#addp3').val();
  var add4 = $('#addp4').val();
  $.post("/handler/update.php", {
      type: 'addpoint',
      add0: add0,
      add1: add1,
      add2: add2,
      add3: add3,
      add4: add4
    },

    function(data) {
      var json = JSON.parse(data);
      var status = json.status;
      var message = json.message;
      if (status) {
        $('#message').html('<span class="invalid-feedback" role="alert" style="color:green"><strong>' + message + '</strong></span><br>');
        $('#point').html(formatMoney.format(json.point));
        $('#addp0').val(0);
        $('#addp1').val(0);
        $('#addp2').val(0);
        $('#addp3').val(0);
        $('#addp4').val(0);
        for (var i = 0; i < 5; i++) {
          $('#point' + i).html('<span id="point' + i + '">' + formatMoney.format(json.pointAdd[i]) + '</span>')
        }
      } else {
        $('#message').html('<span class="invalid-feedback" role="alert" style="color:red"><strong>' + message + '</strong></span><br>');
      }
    });
}

function formBuyAccount(element) {
  $.post("/shop/buy.php", {id:element.id},
    function(data) {
      $('#showForm').html(data);
      $('html, body').animate({
        scrollTop: $("#showForm").offset().top
      }, 500);
    });
}

function buyAcc(id) {
  var password  = $("#password").val();
  var email     = $("#email").val();
  var signature = $("#signature").val();
  if (password == null || password == '' || email == null || email == '' || signature == null || signature == '') {
    swal('Thất bại', 'Không được bổ trống!', 'error');
  } else {
    $.post("/shop/handler.php", {
      id:id,
      password:password,
      email:email,
      signature:signature
    },
    function(data) {
      var json = JSON.parse(data);
      if (json.status) {
        swal('Thành công', json.message, 'success');
        var element = document.getElementById("element" + id);
        element.style.display = 'none';
        huy();
      } else {
        swal('Thất bại', json.message, 'error');
      }
    });
  }
}

function view(user_id, nvId) {
  $.post("/shop/infoequip.php", {
      user_id:user_id,
      nvId: nvId
    },
    function(data) {
      Swal.fire({ 
        title: 'Trang Bị',
        html: "<div style=\"font-size: 8px;\">"  + data + "</div>"
      })
    });
}

function huy() {
  $("#showForm").html('');
}

function topup() {
  var code = $('#code').val();
  var serial = $('#serial').val();
  var telco = $('#telco').val();
  var amount = $('#amount').val();
  var loainap = $('#naploai').val();
  if (code == '' || serial == '' || telco == '' || amount == '' || loainap == '') {
    swal('Thất bại!', 'Vui lòng nhập đủ các trường.', 'error');
    return;
  }
  $.post("/topup/handler.php", {
      code: code,
      serial: serial,
      telco: telco,
      amount: amount,
      loainap: loainap,
    },
    function(data) {
      var m = JSON.parse(data);
      if (m.status == 200) {
        swal('Thành công!', m.message, 'success');
      } else {
        swal('Thất bại!', m.message, 'error');
      }
    });
}