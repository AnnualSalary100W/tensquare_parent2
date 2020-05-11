package com.tensquare.friend.service;


import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;
    public int addFriend(String userid, String friendid) {
        //先判断userid到friend是否有数据，有就是重复添加好友
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(friend!=null){
            return 0;
        }
        //直接添加好友，让好友表中userid到friendid方向type为0
        friend=new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断friendid到userid是否有数据，有数据都改为1
        if(friendDao.findByUseridAndFriendid(friendid,userid)!=null){
            //都改为1
            friendDao.updataIslike("1",friendid,userid);
            friendDao.updataIslike("1",userid,friendid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        //先判断是否已经是非好友
      NoFriend noFriend= noFriendDao.findByUseridAndFriendid(userid,friendid);
      if (noFriend!=null){
          return 0;
      }
      noFriend=new NoFriend();
      noFriend.setUserid(userid);
      noFriend.setFriendid(friendid);
      noFriendDao.save(noFriend);
      return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除表中userid到friend这条数据；
friendDao.deleteFriend(userid,friendid);
        //更新friend到userid的islike为0；
friendDao.updataIslike("0",friendid,userid);
        //非好友表中添加数据；
        NoFriend nofriend = new NoFriend();
        nofriend.setUserid(userid);
        nofriend.setFriendid(friendid);
        noFriendDao.save(nofriend);
    }
}
