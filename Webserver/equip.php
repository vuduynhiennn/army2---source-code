<?php
$title = "Trang Bị - MobiArmy II";
include './include/head.php';
include './include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('Location: /login');
  exit;
}
$nvUsed = $armymem['NVused'] -1;
$ruongTB = json_decode($armymem['ruongTrangBi'], true);
?>
<div class="bg-content">
  <div class="content">
      <h3 style="text-align: center"><b>THÔNG TIN TRANG BI</b></h3>
    <div class="title">
      <h4>Trang bị</h4>
    </div>
    <div class="content">
      <div id="message"></div>
      <? for ($i = 0; $i < count($ruongTB); $i++) {
        if ($ruongTB[$i]['nvId'] != $nvUsed) {
          continue;
        }
        $sql = "SELECT `name`, `isSet` FROM `equip` WHERE `nv` = $nvUsed AND `equipId` = ".$ruongTB[$i]['id']." LIMIT 1";
        $equip = $game->query($sql)->fetch_assoc();
        $equipType = $equip['isSet'] ? 5 : $ruongTB[$i]['equipType'];
        $isUse = $ruongTB[$i]['isUse'] ? "<font id=\"".$i."\" color=\"green\">[mặc]</font>" : "<font id=\"".$i."\" color=\"red\">[chưa]</font>";
        echo '<input type="checkbox" name="'.$equipType.'" id="'.$i.'" onclick="onlyOne(this)">'.$isUse.'CS: '.json_encode($ruongTB[$i]['invAdd']).' %: '.json_encode($ruongTB[$i]['percenAdd']).$equip['name'].'<br />';
      }
      ?>
      <button onclick="equip('mac')">Mặc</button> <button onclick="equip('thao')">Tháo</button><br><br>
    </div>
  </div>
</div>
	
<?php
include './include/end.php';
?>