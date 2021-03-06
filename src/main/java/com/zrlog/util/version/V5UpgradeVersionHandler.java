package com.zrlog.util.version;

import com.zrlog.service.ArticleService;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class V5UpgradeVersionHandler implements UpgradeVersionHandler {

    private static final Logger LOGGER = Logger.getLogger(V5UpgradeVersionHandler.class);

    @Override
    public void doUpgrade(Connection connection) throws SQLException, InterruptedException {
        Thread.sleep(10000L);
        PreparedStatement userPs = connection.prepareStatement("select userid from user limit 1");
        int userId = 0;
        ResultSet userRs = userPs.executeQuery();
        if (userRs.next()) {
            userId = userRs.getInt("userid");
        }
        PreparedStatement ps = connection.prepareStatement("select logid,content from log");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("logid");
            String imgUrl = new ArticleService().getFirstImgUrl(rs.getString("content"), userId);
            PreparedStatement ups = connection.prepareStatement("update log set thumbnail = ? where logid = ?");
            ups.setString(1, imgUrl);
            ups.setInt(2, id);
            ups.execute();
        }
    }
}
