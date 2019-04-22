package com.agiledev.agiledevapp;

public class FullTvEpisodeDetails {
    private String air_date;
    private String episode_number;
    private String name;
    private String overview;
    private String season_number;
    private String still_path;
    private float vote_average;
    private int vote_count;
    private String id;
    private String seriesId;

    public FullTvEpisodeDetails(String air_date, String episode_number, String name, String overview, String season_number, String still_path, float vote_average, int vote_count, String id) {
        this.air_date = air_date;
        this.episode_number = episode_number;
        this.name = name;
        this.overview = overview;
        this.season_number = season_number;
        this.still_path = still_path;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.id = id;
    }

    public String getAir_date() {
        return air_date;
    }
    public String getEpisode_number() {
        return episode_number;
    }
    public String getName() {
        return name;
    }
    public String getOverview() {
        return overview;
    }
    public String getSeason_number() {
        return season_number;
    }
    public String getStill_path() {
        return still_path;
    }
    public float getVote_average() {
        return vote_average;
    }
    public int getVote_count() {
        return vote_count;
    }
    public String getId() {
        return id;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }
}
