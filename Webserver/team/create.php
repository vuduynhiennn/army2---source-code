<?php

if (!$login || !$connect)
    exit(0);
    
include 'res/head.php';

if ($armymem['request_clan'] > 0 || $armymem['clan'] > 0)
    
    echo '<font color="red">* Lỗi: Tài khoản đã là thành viên của đội khác.</font>';
    
else if ($armymem['online'] > 0)
    
    echo '<font color="red">* Lỗi: Tài khoản phải thoát game ra để đăng ký.</font>';
    
else if ($_POST) {
    
    if ($armymem['luong'] < 1000)
        
        echo '<font color="red">* Lỗi: Tài khoản phải có hơn 1000 lượng.</font>';
    
    else {
        
        $clan_name = str_replace(" ", "", $_POST['clan-name']);
        $clan_desc = $_POST['clan-desc'];
        $clan_phone = $_POST['clan-phone'];
        $clan_email = $_POST['clan-email'];
        $clan_icon = $_POST['clan-icon'];
        $time    = date("Y-m-d H:i:s");
        
        if ($clan_name == NULL || $clan_desc == NULL || $clan_phone == NULL || $clan_email == NULL || $clan_icon == NULL) 
            
            echo '<font color="red">* Lỗi: Thông tin điền vào không đủ.</font>';
        
        if (strlen($clan_name)< 5 || strlen($clan_name) > 15) 
            
            echo '<font color="red">* Lỗi: Tên đội phải hơn 5 và dưới 15 kí tự.</font>';
        
        else if (!preg_match('/^[0-9]+$/',$clan_phone.$clan_icon))
    
            echo '<font color="red">* Lỗi: Có một số kí tự không được phép vui lòng kiểm tra lại.</font>';
    
        else  if ($conn->query("SELECT `id` FROM `clan` WHERE `name` = '". $conn->real_escape_string($clan_name) ."' ;")->num_rows > 0)
        
            echo '<font color="red">* Lỗi: Tên biệt đội đã tồn tại.</font>';
        
        else {
         
            $sql = "INSERT INTO `clan` (`name`, `master`, `masterName`,`desc` ,`phone`,`email`,`icon`, `dateCreat`)
             VALUES 
            ('". $conn->real_escape_string($clan_name) ."', '". $user['user_id'] ."', '". $user['user'] ."', '". $conn->real_escape_string($clan_desc) ."', '". $clan_phone ."', '". $conn->real_escape_string($clan_email) . "', '". $clan_icon ."', '". $time ."')";
            $conn->query($sql);
            
            $sql = "SELECT `id` FROM `clan` WHERE `name` = '". $conn->real_escape_string($clan_name) ."' LIMIT 1;";
            $result = $conn->query($sql);
            if ($result->num_rows > 0) {
                $CL = $result->fetch_assoc();
            
                $sql = "INSERT INTO `clanmem` (`clan`, `user`, `user_name`, `rights`) VALUES ('". $CL['id'] ."', '". $user['user_id'] ."', '". $user['user'] ."', 2)";
                $conn->query($sql);
                
                $sql = "UPDATE `armymem` SET `luong` = `luong` - 1000, `clan` = '". $CL['id'] . "', `request_clan` = '". $CL['id'] ."' WHERE `id` = '". $armymem['id'] ."'; ";
                $conn->query($sql);
                echo '<font color="blue">* Tạo đội thành công <a href="?c=list">vào ngày Đội.</a></font>';
    
            }
        }
        
        
    }

} else {

?>
<form action="" method="post">
	
    <h3>Đăng ký thành lập đội</h3>
		  <h3>Lưu ý: cần điền thông tin chính xác để lấy lại mật khẩu.</h3>
		  
		  <table>
		
        <tbody>		
            <tr>
		
                <td>Tên đội (*):</td>
                <td><input type="text" name="clan-name"></td>
		
            </tr>
            <tr>
		            
		              <td>Giới thiệu:</td>          
		            <td><textarea name="clan-desc" rows="2" cols="40"></textarea></td>
   
		          </tr>
		          <tr>
		              
		              <td>Số điện thoại (*):</td>
		              <td><input type="tel" name="clan-phone" size="14"></td>

            </tr>
            <tr>
                
                  <td>Email (*) (Ví dụ: abc@gmail.com):</td>
                  <td><input type="email" name="clan-email" size="20"></td>
                  
            </tr>
            <tr>
            
                <td colspan="2">Hãy lựa chọn ảnh đại diện cho đội:<br>
                    <table width="100%">
<?php
$ids = 0;
for ($i = 1; $i <= 20; $i++) {
    if ($ids > 188)
        break;
?>
                        <tr>
<?php
    for ($j = 1; $j <= 10; $j++) {
        $ids += 1;
        if ($ids > 188)
            break;
?>
                            <td style="border: 1px solid #AAA;">
                                <img src="res/icon/<?php echo $ids;?>.png">
                                <input type="radio" name="clan-icon" value="<?php echo $ids;?>">&nbsp;&nbsp;&nbsp;
                            </td>
<?php
    }
?>
                         </tr>
<?php
}
?>
                    </tbody>
                </td>
                
            </tr>
        <table>
    </table>
    <input type="submit" name="submit" value="Xác nhận">
</form>

<?php

}
echo "\n" . '<br />';
include 'res/end.php';
?>