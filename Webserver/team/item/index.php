<?php
if (!$login || !$connect)
    exit(0);
if ($armymem['clan'] > 0 && $clanmem['rights'] > 0) {
    $RuongItem = json_decode($clan['Item'], true);
    echo '<div style="font-weight:bolder">Danh sách item</div>';
    $time_now = date("Y-m-d H:i:s");
    foreach ($RuongItem as $key => $Item) {
        $result = $conn->query("SELECT * FROM `clanshop` WHERE  `id` = '". $Item['id'] ."' LIMIT 1");
        $ClanShop = $result->fetch_assoc();
        $isItem =strtotime($Item['time']) - strtotime($time_now) > 0;
        echo '<hr />'. ($key+1) .'. <b>'. ($result->num_rows>0?$ClanShop["name"]:('ITEM '. $Item["id"])) .'</b><br />Cấp độ yêu cầu: '. $ClanShop["level"];
        echo '<div>Hạn dùng: <b>'. ($isItem ? (string_tempo_restas($time_now, $Item["time"])) : 'Đã hết') .'</b> (<a style="color:red;font-weight:bolder" href="item/clear.php?id='. $Item["id"] .'">Hủy</a>)</div>';
        echo '<hr /><br />';
    }
}
?>