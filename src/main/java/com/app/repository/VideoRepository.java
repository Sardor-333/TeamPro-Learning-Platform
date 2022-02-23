package com.app.repository;

import com.app.model.Video;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
@Transactional
@EnableTransactionManagement
public class VideoRepository implements BaseRepository<Video, UUID> {
    private Session session;

    public VideoRepository(LocalSessionFactoryBean localSessionFactoryBean) {
        this.session = Objects.requireNonNull(localSessionFactoryBean.getObject()).openSession();
    }

    @Override
    public List<Video> getAll() {
        Query from_videos = session.createQuery("from videos");
        return from_videos.list();
    }

    @Override
    public Video getById(UUID id) {
        return session.get(Video.class,id);
    }

    @Override
    public boolean existsById(UUID id) {
        return getById(id) != null;
    }

    @Override
    public void save(Video elem) {
        session.save(elem);
    }

    @Override
    public void update(Video elem) {
        session.update(elem);
    }

    @Override
    public void saveOrUpdate(Video elem) {
        if (elem.getId()!= null) {
            session.update(elem);
        }else {
            session.save(elem);
        }

    }

    @Override
    public Video deleteById(UUID id) {
        Video video = getById(id);
        session.delete(video);
        return video;
    }

    @Override
    public void clear() {
        Query delete_from_videos = session.createQuery("delete from videos");
    }
}
