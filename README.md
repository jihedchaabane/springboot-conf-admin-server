# springboot-conf-admin-server
-----------------------------------------------------
IN "10.0.0.129" do:
-----------------------------------------------------
sudo firewall-cmd --add-port=8763/tcp --permanent
sudo firewall-cmd --reload

sudo firewall-cmd --list-ports
sudo firewall-cmd --list-all

---Fermer le port 8763 -------------------------------
sudo firewall-cmd --permanent --remove-port=8763/tcp
sudo firewall-cmd --reload

sudo firewall-cmd --list-ports
sudo firewall-cmd --list-all
-----------------------------------------------------