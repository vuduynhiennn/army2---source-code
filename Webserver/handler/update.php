<?php
if (isset($_POST)) {
  include '../include/config.php';
  include '../include/connect.php';
  if (!isset($_SESSION['user_id'])) {
    $msg = array("status" => false, "message" => "Bạn chưa đăng nhập!");
    echo json_encode($msg);
    return;
  }
  $nv = json_decode($armymem['NV'.$armymem['NVused']],true);
  $type = $_POST['type'];
  switch ($type) {
    case 'addpoint':
      $point = $nv['point'];
      $pointAdd = $nv['pointAdd'];
      $totalPoint = 0;
      for ($i = 0; $i < 5; $i++) {
        $add = $_POST['add'.$i];
        if ($add == null || $add == "") {
          $add = 0;
        }
        if (!preg_match('/^[0-9]+$/',$add)) {
          $msg = array("status" => false, "message" => "Lỗi kí tự!");
          echo json_encode($msg);
          return;
        }
        if ($add > 1000) {
          $msg = array("status" => false, "message" => "Mỗi dòng tối đa 1000!");
          echo json_encode($msg);
          return;
        }
        if ($add < 0) {
          $msg = array("status" => false, "message" => "Nhập không hợp lệ!");
          echo json_encode($msg);
          return;
        }
        $pointAdd[$i] += $add;
        $totalPoint += $add;
      }
      if ($totalPoint <= 0) {
        $msg = array("status" => false, "message" => "Vui lòng nhập số điểm cộng hợp lệ!");
        echo json_encode($msg);
        return;
      }
      if ($totalPoint > $point) {
        $msg = array("status" => false, "message" => "Không đủ điểm cộng!");
        echo json_encode($msg);
        return;
      }
      $point -= $totalPoint;
      $nv["pointAdd"] = $pointAdd;
      $nv["point"] = $point;
      $armymem['NV'.$armymem['NVused']] = json_encode($nv);
      $sql = "UPDATE `armymem` SET `NV" . $armymem['NVused'] . "` = '" .$armymem['NV'.$armymem['NVused']]."' WHERE `id` = " .$armymem['id']. "; ";
      if ($game->query($sql)) {
        $msg = array("status" => true, "message" => "Cộng thành công!", "point" => $point, "pointAdd" => $nv[pointAdd]);
        echo json_encode($msg);
      } else {
        $msg = array("status" => false, "message" => "Xảy ra lỗi!");
        echo json_encode($msg);
      }
      break;
      
      case 'loadDataItem':
        $id = $_POST['idItem'];
        $sql = "SELECT * FROM `specialitem` WHERE `id` = ".$game->real_escape_string($id).";";
        $item = $game->query($sql)->fetch_assoc();
        echo '<div class="title" title="Thanh toán">Thanh toán</div>';
        echo '<div class="content">';
        echo '<div id="message"></div>';
        echo '<a>Bạn đang có: <span id="haveXu" class="badge badge-light">'.number_format($armymem['xu'], 0, '.', '.').' xu</span> - <span id="haveLuong" class="badge badge-light">'.number_format($armymem['luong'], 0, '.', '.').' lượng</span></a><br>';
        echo '<a>Tên: '.$item['name'].'</a><br>';
        if ($item['giaXu'] > 0) {
          echo '<a>Giá xu: <span id="xu" class="badge badge-light">'.number_format($item['giaXu'], 0, '.', '.').'</span></a><br>';
        }
        if ($item['giaLuong'] > 0) {
          echo '<a>Giá lượng:  <span id="luong" class="badge badge-light">'.number_format($item['giaLuong'], 0, '.', '.').'</span></a><br>';
        }
        echo '<div class="input-group input-group-sm mb-3">';
        echo '<div class="input-group-prepend">';
        echo '<span class="input-group-text" id="inputGroup-sizing-sm">Số lượng</span>';
        echo '</div>';
        echo '<input id="numItem" value="1" onkeyup="pricing('.$item['giaXu'].', '.$item['giaLuong'].')" type="number" class="form-control" aria-label="Số lượng" aria-describedby="inputGroup-sizing-sm">';
        echo '</div>';
        if ($item['giaXu'] > 0) {
          echo '<button onclick="buyItem(0, '.$item['id'].')" type="button" class="btn btn-outline-primary">Mua bằng xu</button>';
        }
        if ($item['giaLuong'] > 0) {
          echo '<button onclick="buyItem(1, '.$item['id'].')" type="button" class="btn btn-outline-primary">Mua bằng lượng</button>';
        }
        echo '</div>';
        break;
        
      case 'buyItem':
        $buyType = $_POST['buyType'];
        $idItem = $_POST['idItem'];
        $num = $_POST['num'];
        if ($type == null || !preg_match('/^[0-9]+$/',$buyType) || ($buyType != 0 && $buyType != 1)) {
          $msg = array("status" => false, "message" => "Phương thức thanh toán không hợp lệ!");
          echo json_encode($msg);
          return;
        }
        if (!preg_match('/^[0-9]+$/',$idItem)) {
          $msg = array("status" => false, "message" => "Vật phẩm không hợp lệ!");
          echo json_encode($msg);
          return;
        }
        if (!preg_match('/^[0-9]+$/',$num)) {
          $msg = array("status" => false, "message" => "Số lượng không hợp lệ!");
          echo json_encode($msg);
          return;
        }
        if ($num > 1000) {
          $msg = array("status" => false, "message" => "Số lượng tối đa 1000!");
          echo json_encode($msg);
          return;
        } else if ($num < 1) {
          $msg = array("status" => false, "message" => "Số lượng tối thiểu là 1!");
          echo json_encode($msg);
          return;
        }
        $sql = "SELECT * FROM `specialitem` WHERE `id` = ".$game->real_escape_string($idItem).";";
        $item = $game->query($sql)->fetch_assoc();
        if (!$item['onSale']) {
          $msg = array("status" => false, "message" => "Vật phẩm này không bán!");
          echo json_encode($msg);
          return;
        }
        $price = $num * (($buyType == 0) ? $item['giaXu'] : $item['giaLuong']);
        if ($price < 0) {
          $msg = array("status" => false, "message" => "Có lỗi xảy ra!");
          echo json_encode($msg);
          return;
        }
        if ((($buyType == 0) ? $armymem['xu'] : $armymem['luong']) < $price) {
          $msg = array("status" => false, "message" => "Không đủ tiền!");
          echo json_encode($msg);
          return;
        }
        if ($buyType == 0) {
          $sql = "UPDATE `armymem` SET `xu` = `xu` - ".$price." WHERE `id` = " .$armymem['id']. "; ";
          if (!$game->query($sql)) {
            $msg = array("status" => false, "message" => "Xảy ra lỗi!");
            echo json_encode($msg);
            return;
          }
          $armymem['xu'] -= $price;
        } else {
          $sql = "UPDATE `armymem` SET `luong` = `luong` - ".$price." WHERE `id` = " .$armymem['id']. "; ";
          if (!$game->query($sql)) {
            $msg = array("status" => false, "message" => "Xảy ra lỗi!");
            echo json_encode($msg);
            return;
          }
          $armymem['luong'] -= $price;
        }
        $ruongItem = json_decode($armymem['ruongItem'], true);
        $isExist = false;
        foreach ($ruongItem as $key => $value) {
          if ($value['id'] == $idItem) {
            if (($value['numb'] + $num) > 32767) {
              $msg = array("status" => false, "message" => "Sức chứa của vật phẩm này đã đầy!");
              echo json_encode($msg);
              return;
            }
            $ruongItem[$key]['numb'] += $num;
            $isExist = true;
            break;
          }
        }
        if (!$isExist) {
          $itemData = array("id" => ((int) $idItem), "numb" => ((int) $num));
          $ruongItem[] = $itemData;
        }
        $sql = "UPDATE `armymem` SET `ruongItem` = '".json_encode($ruongItem)."' WHERE `id` = " .$armymem['id']. "; ";
        if ($game->query($sql)) {
          $msg = array("status" => true, "message" => "Mua vật phẩm thành công!", "xu" => $armymem['xu'], "luong" => $armymem['luong']);
          echo json_encode($msg);
        } else {
          $msg = array("status" => false, "message" => "Xảy ra lỗi!");
          echo json_encode($msg);
        }
        break;
        
      case 'specialStore':
        $sql = "SELECT `id`, `name` FROM `specialitem`;";
        $item = $game->query($sql);
        echo '<div class="input-group mb-3">
          <div class="input-group-prepend">
            <label class="input-group-text" for="item">Vật phẩm</label>
          </div>
          <select class="custom-select" id="item">';
        foreach ($item as $value) {
          echo '<option value="'.$value['id'].'">'.$value['name'].'</option>';
        }
        echo '</select></div>';
        echo '<button class="btn btn-primary" onclick="addItemToStore()">Thêm</button>';
        break;
        
      case 'storeEquipment':
        $figure = $_POST['figure'];
        if ($figure == null || !preg_match('/^[0-9]+$/',$figure)) {
          $figure = 0;
        }
        $sql = "SELECT `id`, `name` FROM `equip` WHERE `nv` = ".$figure.";";
        $item = $game->query($sql);
        echo '<div class="input-group mb-3">
          <div class="input-group-prepend">
            <label class="input-group-text" for="item">Trang bị</label>
          </div>
          <select class="custom-select" id="item">';
        foreach ($item as $value) {
          echo '<option value="'.$value['id'].'">'.$value['name'].'</option>';
        }
        echo '</select></div>';
        echo '<div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text">Chỉ số</span>
          </div>
          <input id="invAdd" type="text" value="[0,0,0,0,0]" class="form-control" aria-label="Chỉ số">
        </div>';
        echo '<div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text">Chỉ số (%) </span>
          </div>
          <input id="percenAdd" type="text" value="[0,0,0,0,0]" class="form-control" aria-label="Chỉ số (%)">
        </div>';
        echo '<button class="btn btn-primary" onclick="addItemToStore()">Thêm</button>';
        break;
        
        case 'addItemToStore':
          $itemType = $_POST['itemType'];
          $itemId = $_POST['itemId'];
          $itemPrice = $_POST['itemPrice'];
          $itemAmount = $_POST['itemAmount'];
          $itemInvAdd = $_POST['itemInvAdd'];
          $itemPercenAdd = $_POST['itemPercenAdd'];
          if ($itemType == null || $itemId == null || $itemPrice == null || $itemAmount == null) {
            $msg = array("status" => false, "message" => "Xảy ra lỗi!");
            echo json_encode($msg);
            return;
          }
          if (!preg_match('/^[0-9]+$/',$itemId) || !preg_match('/^[0-9]+$/',$itemAmount) || !preg_match('/^[0-9]+$/',$itemPrice)) {
            $msg = array("status" => false, "message" => "Ký tự không hợp lệ!");
            echo json_encode($msg);
            return;
          }
          $itemObj = array();
          if ($itemType == 'specialitem') {
            $sql = "SELECT `name` FROM `specialitem` WHERE `id` = ".$itemId.";";
            $getItem = $game->query($sql)->fetch_assoc();
            $itemObj = [
              "id" => (int) $itemId, 
              "name" => (string) $getItem['name']
              ];
          } else {
            $invAdd = json_decode($itemInvAdd);
            $percenAdd = json_decode($itemPercenAdd);
            if ($invAdd == null || $percenAdd == null || !is_array($invAdd) || !is_array($percenAdd) || count($invAdd) != 5 || count($percenAdd) != 5) {
              $msg = array("status" => false, "message" => "Chỉ số của trang bị không hợp lệ!");
              echo json_encode($msg);
              return;
            }
            $sql = "SELECT `frame`, `name`, `equipId`, `nv`, `equipType` FROM `equip` WHERE `id` = ".$itemId.";";
            $getEquip = $game->query($sql)->fetch_assoc();
            $itemObj = [
              "id" => (int) $getEquip['equipId'],
              "nv" => (int) $getEquip['nv'],
              "type" => (int) $getEquip['equipType'],
              "frame" => (int) $getEquip['frame'],
              "name" => (string) $getEquip['name'],
              "invAdd" => (string) $itemInvAdd,
              "percenAdd" => (string) $itemPercenAdd
              ];
          }
          $sql = "INSERT INTO `shop` (`id`, `type`, `amount`, `price`, `data`) VALUES ('', '".$itemType."', ".$itemAmount.", ".$itemPrice.", '".$game->real_escape_string(json_encode($itemObj))."')";
          if ($game->query($sql)) {
            $msg = array("status" => true, "message" => "Thêm vật phẩm vào của hàng thành công!");
            echo json_encode($msg);
          } else {
            $msg = array("status" => false, "message" => "Thêm vật phẩm vào của hàng thất bại!");
            echo json_encode($msg);
          }
          break;
          
        case "mac":
          $index = $_POST['index'];
          if ($index != null) {
            $list = $index;
            $error = 0;
            if (count($list) == 0) {
              $msg = array("status" => false, "message" => "Vui lòng chọn trang bị!");
              echo json_encode($msg);
              return;
            } 
            $ruongTB = json_decode($armymem["ruongTrangBi"], true);
            $nvId = $armymem['NVused'] -1;
            $data = json_decode($armymem['NV'.($nvId + 1)], true);
            for ($i = 0; $i < count($list); $i++) {
              if ($list[$i] < 0 || $list[$i] > count($ruongTB)) {
                continue;
              }
              $equip = $ruongTB[(int) $list[$i]];
              if ($equip['nvId'] == $nvId && !$equip['isUse']) {
                $temp = $game->query("SELECT `lvRequire`, `isSet` FROM `equip` WHERE `equipId` = ".$equip['id']." AND `nv` = ".$equip['nvId']." LIMIT 1;")->fetch_assoc();
                if ($temp['lvRequire'] > $data['lever']) {
                  $error++;
                  continue;
                }
                if ($temp['isSet']) {
                  if ($data['data'][5] > -1) {
                    $ruongTB[$data['data'][5]]['isUse'] = false;
                    $data['data'][5] = -1;
                  }
                  $equip['isUse'] = true;
                  $ruongTB[(int) $list[$i]] = $equip;
                  $data['data'][5] = (int) $list[$i];
                } else {
                  if ($data['data'][(int) $equip['equipType']] > -1) {
                    $ruongTB[$data['data'][(int) $equip['equipType']]]['isUse'] = false;
                    $data['data'][(int) $equip['equipType']] = -1;
                  }
                  $equip['isUse'] = true;
                  $ruongTB[(int) $list[$i]] = $equip;
                  $data['data'][(int) $equip['equipType']] = (int) $list[$i];
                }
              }
            }
            if (count($list) == $error) {
              $msg = array("status" => false, "message" => "Trang bị cấp quá cao! Vui lòng chiến đấu để đạt đủ cấp độ.");
              echo json_encode($msg);
              return;
            }
            if ($game->query("UPDATE `armymem` SET `ruongTrangBi` = '".json_encode($ruongTB)."', `NV".($nvId + 1)."` = '".json_encode($data)."' WHERE `id` = ".$user['user_id']." LIMIT 1;")) {
              $msg = array("status" => true, "message" => "Mặc thành công!", "data" => json_encode($data['data']));
              echo json_encode($msg);
            } else {
              $msg = array("status" => false, "message" => "Có lỗi xảy ra!");
              echo json_encode($msg);
            }
          }
          break;
         
        case "thao":
          $index = $_POST['index'];
          if ($index != null) {
            $list = $index;
            if (count($list) == 0) {
              $msg = array("status" => false, "message" => "Vui lòng chọn trang bị!");
              echo json_encode($msg);
              return;
            } 
            $ruongTB = json_decode($armymem["ruongTrangBi"], true);
            $nvId = $armymem['NVused'] -1;
            $data = json_decode($armymem['NV'.($nvId + 1)], true);
            for ($i = 0; $i < count($list); $i++) {
              if ($list[$i] < 0 || $list[$i] > count($ruongTB)) {
                continue;
              }
              $equip = $ruongTB[(int) $list[$i]];
              if ($equip['nvId'] == $nvId && $equip['isUse']) {
                $ruongTB[(int) $list[$i]]['isUse'] = false;
                $temp = $game->query("SELECT `isSet` FROM `equip` WHERE `equipId` = ".$equip['id']." AND `nv` = ".$equip['nvId']." LIMIT 1;")->fetch_assoc();
                if ($temp['isSet']) {
                  $data['data'][5] = -1;
                } else {
                  $data['data'][$equip['equipType']] = -1;
                }
              }
            }
            if ($game->query("UPDATE `armymem` SET `ruongTrangBi` = '".json_encode($ruongTB)."', `NV".($nvId + 1)."` = '".json_encode($data)."' WHERE `id` = ".$user['user_id']." LIMIT 1;")) {
              $msg = array("status" => true, "message" => "Tháo thành công!", "data" => json_encode($data['data']));
              echo json_encode($msg);
            } else {
              $msg = array("status" => false, "message" => "Có lỗi xảy ra!");
              echo json_encode($msg);
            }
          }
          break;
  }
}
?>