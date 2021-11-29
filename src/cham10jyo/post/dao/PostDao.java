package cham10jyo.post.dao;

import cham10jyo.post.domain.Post;
import cham10jyo.post.dto.PostAddDto;
import cham10jyo.post.dto.PostEditDto;
import cham10jyo.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class PostDao {
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public PostDao() {
        connection = DbUtil.getConnection();
    }


    /**
     *  게시글 생성
     * @param postAddDto 제목
     * @return 게시글 생성 성공 여부
     */
    public boolean write(PostAddDto postAddDto) {
        String SQL = "insert into post values(?, ?, ?, ?, ?, ?)";
        try {
            java.sql.Date today = (java.sql.Date) new Date();
            pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, postAddDto.getTitle());
            pstmt.setString(2, postAddDto.getUserId());
            pstmt.setDate(3, today);
            pstmt.setDate(4, today);
            pstmt.setString(5, postAddDto.getContent());
            pstmt.setInt(6, 0); // 0 - false, 1 - true 삭제 여부
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; //데이터베이스 오류
        }
    }


    /**
     * 게시글 수정
     * @param id 수정할 게시글을 찾기 위한 id
     * @param postEditDto 제목이 title로 수정됨
     * @return
     */
    public boolean editContent(PostEditDto postEditDto, Long id) {
        try {
            java.sql.Date today = (java.sql.Date) new Date();
            pstmt = connection.prepareStatement("update post set title=?, updated_date=?,content=? where id=?");
            pstmt.setString(1, postEditDto.getTitle());
            pstmt.setDate(2, today);
            pstmt.setString(3, postEditDto.getContent());
            pstmt.setLong(4, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; //데이터베이스 오류
        }
    }


    /**
     * 게시글 soft 삭제 (삭제된 것으로 보이게하기)
     * @param id
     * @return
     */
    public boolean deleteSoft(Long id) {
        try {
            pstmt = connection.prepareStatement("update post set isRemoved=? where id=?");
            pstmt.setLong(1, 1); // 1이면 삭제됨 의미 db에 tinyint
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; //데이터베이스 오류
        }
    }


    /**
     * 게시글 hard 삭제
     * @param id
     * @return
     */
    public boolean deleteHard(Long id) {
        try {
            pstmt = connection.prepareStatement("delete from post where id = ?");
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; //데이터베이스 오류
        }
    }


    /**
     * 사용자 이름으로 게시글 검색
     * @param name
     * @return
     */
    public ArrayList<Post> searchByUserName(String name) {
        try {
            ArrayList<Post> posts = new ArrayList<Post>();
            pstmt = connection.prepareStatement("select * from post where id like '%?%'");
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getLong(1));
                post.setTitle(rs.getString(2));
                post.setUserId(rs.getString(3));
                post.setCreatedDate(rs.getDate(4));
                post.setUpdatedDate(rs.getDate(5));
                post.setContent(rs.getString(6));
                post.setRemoved(rs.getBoolean(6));
                posts.add(post);
            }
            pstmt.executeUpdate();
            return posts;
        } catch (Exception e) {
            e.printStackTrace();
            return null; //데이터베이스 오류
        }
    }


    /**
     * title로 게시글 검색
     * @param title
     * @return
     */
    public ArrayList<Post> searchByTitle(String title) {
        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            pstmt = connection.prepareStatement("select * from post where title like '%?%'");
            pstmt.setString(1, title);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Post post = new Post();
                post.setId(rs.getLong(1));
                post.setTitle(rs.getString(2));
                post.setUserId(rs.getString(3));
                post.setCreatedDate(rs.getDate(4));
                post.setUpdatedDate(rs.getDate(5));
                post.setRemoved(rs.getBoolean(6));
                posts.add(post);
            }
            return posts;
        } catch (Exception e) {
            e.printStackTrace();
            return null; //데이터베이스 오류
        }
    }


    /**
     * 모든 게시물 불러오기
     * @return
     */
    public ArrayList<Post> getAllPost() {
        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            pstmt = connection.prepareStatement("select * from post");
            rs = pstmt.executeQuery();
            while (rs.next()){
                Post post = new Post();
                post.setId(rs.getLong(1));
                post.setTitle(rs.getString(2));
                post.setUserId(rs.getString(3));
                post.setCreatedDate(rs.getDate(4));
                post.setUpdatedDate(rs.getDate(5));
                post.setRemoved(rs.getBoolean(6));
                posts.add(post);
            }
            return posts;
        } catch (Exception e) {
            e.printStackTrace();
            return null; //데이터베이스 오류
        }
    }
}
