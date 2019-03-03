package com.agiledev.agiledevapp;

import java.util.ArrayList;

/**
 * Created by Tom on 02/03/2019.
 */

public class FullTvShowDetails
{

    private String backdrop_path;
    private ArrayList<EpisodeRunTime> episode_run_time;
    private String first_air_date;
    private ArrayList<Genre> genres;
    private String homepage;
    private String last_air_date;
    private ArrayList<LastEpisodeToAir> last_episode_to_air;
    private String name;
    //private null next_episode_to_air;
    private int number_of_episodes;
    private int number_of_seasons;
    private String overview;
    private String poster_path;
    private ArrayList<TVProductionCompanies> production_companies;
    private ArrayList<season> seasons;
    private String status;
    private String type;
    private Number vote_average;


    private Credits credits;

    public FullTvShowDetails() {}

    public FullTvShowDetails(String backdrop_path, ArrayList<EpisodeRunTime> episode_run_time,
                             String first_air_date, ArrayList<Genre> genres, String homepage,
                             String last_air_date, ArrayList<LastEpisodeToAir> last_episode_to_air,
                             String name, int number_of_episodes, int number_of_seasons,
                             String overview, String poster_path,
                             ArrayList<TVProductionCompanies> production_companies,
                             ArrayList<season> seasons, String status,
                             String type, Number vote_average) {

        this.backdrop_path = backdrop_path;
        this.episode_run_time = episode_run_time;
        this.genres = genres;
        this.homepage = homepage;
        this.last_air_date = last_air_date;
        this.last_episode_to_air = last_episode_to_air;
        this.name = name;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this .overview = overview;
        this.poster_path = poster_path;
        this.production_companies = production_companies;
        this.seasons = seasons;
        this.status = status;
        this.type = type;
        this.vote_average = vote_average;

    }


    public String getBackdrop_path() {
        return backdrop_path;
    }

    public ArrayList<EpisodeRunTime> getRunTime() {
        return episode_run_time;
    }

    public String getFirst_air_date() { return first_air_date; }

    public ArrayList<Genre> getGenres() { return genres; }

    public String getHomepage() { return homepage; }

    public String getLast_air_date() { return last_air_date; }

    public ArrayList<LastEpisodeToAir> getLast_episode_to_air() { return last_episode_to_air; }

    public String getName() { return name; }

    public int getNumber_of_episodes() { return number_of_episodes; }

    public int getNumber_of_seasons() { return number_of_seasons; }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public ArrayList<TVProductionCompanies> getProduction_companies() { return production_companies; }

    public ArrayList<season> getSeason() { return seasons; }

    public String getStatus() {
        return status;
    }

    public String getType() { return type; }

    public Number getVote_average() { return vote_average; }

    public ArrayList<Cast> getCast() {
        return credits.getCast();
    }



    public class season
    {
        String air_date;
        int episode_count;
        int id;
        String name;
        String overview;
        String poster_path;
        int season_number;
    }

    public class TVProductionCompanies
    {
        int id;
        String logo_path;
        String name;
        String origin_country;
    }

    public class Genre
    {
        int id;
        String name;
    }

    public class LastEpisodeToAir
    {
        String air_date;
        int episode_number;
        int id;
        String name;
        String overview;
        String production_code;
        int season_number;
        int show_id;
        String still_path;
        Number vote_average;
        int vote_count;
    }

    public class Credits {
        ArrayList<Cast> cast;

        public ArrayList<Cast> getCast() {
            return cast;
        }
    }

    public class Cast
    {
        private String character;
        private String credit_id;
        private int gender;
        private int id;
        private String name;
        private String profile_path;

        public String getcredit_id() {
            return credit_id;
        }
        public String getCharacter() {
            return character;
        }
        public int getGender() {
            return gender;
        }
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getProfile_path() {
            return profile_path;
        }
    }

    public String getGenresString() {
        ArrayList<Genre> genres = getGenres();
        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            returnString.append(genres.get(i).name);
            if (i != genres.size() - 1)
                returnString.append(" / ");
        }
        return returnString.toString();
    }

    public class EpisodeRunTime
    {
        int episode_run_time;
    }

}
