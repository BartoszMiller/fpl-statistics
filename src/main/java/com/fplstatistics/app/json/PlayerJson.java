package com.fplstatistics.app.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PlayerJson implements Comparable<PlayerJson> {

    public Object chance_of_playing_next_round;
    public Object chance_of_playing_this_round;
    public int code;
    public int cost_change_event;
    public int cost_change_event_fall;
    public int cost_change_start;
    public int cost_change_start_fall;
    public int dreamteam_count;
    public int element_type;
    public String ep_next;
    public Object ep_this;
    public int event_points;
    public String first_name;
    public String form;
    public int id;
    public boolean in_dreamteam;
    public String news;
    public Object news_added;
    public int now_cost;
    public String photo;
    public String points_per_game;
    public String second_name;
    public String selected_by_percent;
    public boolean special;
    public Object squad_number;
    public String status;
    public int team;
    public int team_code;
    public int total_points;
    public int transfers_in;
    public int transfers_in_event;
    public int transfers_out;
    public int transfers_out_event;
    public String value_form;
    public String value_season;
    public String web_name;
    public int minutes;
    public int goals_scored;
    public int assists;
    public int clean_sheets;
    public int goals_conceded;
    public int own_goals;
    public int penalties_saved;
    public int penalties_missed;
    public int yellow_cards;
    public int red_cards;
    public int saves;
    public int bonus;
    public int bps;
    public String influence;
    public String creativity;
    public String threat;
    public String ict_index;
    public int influence_rank;
    public int influence_rank_type;
    public int creativity_rank;
    public int creativity_rank_type;
    public int threat_rank;
    public int threat_rank_type;
    public int ict_index_rank;
    public int ict_index_rank_type;

    public TeamJson teamJsonObject;

    public Object getChance_of_playing_next_round() {
        return chance_of_playing_next_round;
    }

    public void setChance_of_playing_next_round(Object chance_of_playing_next_round) {
        this.chance_of_playing_next_round = chance_of_playing_next_round;
    }

    public Object getChance_of_playing_this_round() {
        return chance_of_playing_this_round;
    }

    public void setChance_of_playing_this_round(Object chance_of_playing_this_round) {
        this.chance_of_playing_this_round = chance_of_playing_this_round;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCost_change_event() {
        return cost_change_event;
    }

    public void setCost_change_event(int cost_change_event) {
        this.cost_change_event = cost_change_event;
    }

    public int getCost_change_event_fall() {
        return cost_change_event_fall;
    }

    public void setCost_change_event_fall(int cost_change_event_fall) {
        this.cost_change_event_fall = cost_change_event_fall;
    }

    public int getCost_change_start() {
        return cost_change_start;
    }

    public void setCost_change_start(int cost_change_start) {
        this.cost_change_start = cost_change_start;
    }

    public int getCost_change_start_fall() {
        return cost_change_start_fall;
    }

    public void setCost_change_start_fall(int cost_change_start_fall) {
        this.cost_change_start_fall = cost_change_start_fall;
    }

    public int getDreamteam_count() {
        return dreamteam_count;
    }

    public void setDreamteam_count(int dreamteam_count) {
        this.dreamteam_count = dreamteam_count;
    }

    public int getElement_type() {
        return element_type;
    }

    public void setElement_type(int element_type) {
        this.element_type = element_type;
    }

    public String getEp_next() {
        return ep_next;
    }

    public void setEp_next(String ep_next) {
        this.ep_next = ep_next;
    }

    public Object getEp_this() {
        return ep_this;
    }

    public void setEp_this(Object ep_this) {
        this.ep_this = ep_this;
    }

    public int getEvent_points() {
        return event_points;
    }

    public void setEvent_points(int event_points) {
        this.event_points = event_points;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIn_dreamteam() {
        return in_dreamteam;
    }

    public void setIn_dreamteam(boolean in_dreamteam) {
        this.in_dreamteam = in_dreamteam;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public Object getNews_added() {
        return news_added;
    }

    public void setNews_added(Object news_added) {
        this.news_added = news_added;
    }

    public int getNow_cost() {
        return now_cost;
    }

    public void setNow_cost(int now_cost) {
        this.now_cost = now_cost;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPoints_per_game() {
        return points_per_game;
    }

    public void setPoints_per_game(String points_per_game) {
        this.points_per_game = points_per_game;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getSelected_by_percent() {
        return selected_by_percent;
    }

    public void setSelected_by_percent(String selected_by_percent) {
        this.selected_by_percent = selected_by_percent;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public Object getSquad_number() {
        return squad_number;
    }

    public void setSquad_number(Object squad_number) {
        this.squad_number = squad_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getTeam_code() {
        return team_code;
    }

    public void setTeam_code(int team_code) {
        this.team_code = team_code;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public int getTransfers_in() {
        return transfers_in;
    }

    public void setTransfers_in(int transfers_in) {
        this.transfers_in = transfers_in;
    }

    public int getTransfers_in_event() {
        return transfers_in_event;
    }

    public void setTransfers_in_event(int transfers_in_event) {
        this.transfers_in_event = transfers_in_event;
    }

    public int getTransfers_out() {
        return transfers_out;
    }

    public void setTransfers_out(int transfers_out) {
        this.transfers_out = transfers_out;
    }

    public int getTransfers_out_event() {
        return transfers_out_event;
    }

    public void setTransfers_out_event(int transfers_out_event) {
        this.transfers_out_event = transfers_out_event;
    }

    public String getValue_form() {
        return value_form;
    }

    public void setValue_form(String value_form) {
        this.value_form = value_form;
    }

    public String getValue_season() {
        return value_season;
    }

    public void setValue_season(String value_season) {
        this.value_season = value_season;
    }

    public String getWeb_name() {
        return web_name;
    }

    public void setWeb_name(String web_name) {
        this.web_name = web_name;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getGoals_scored() {
        return goals_scored;
    }

    public void setGoals_scored(int goals_scored) {
        this.goals_scored = goals_scored;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getClean_sheets() {
        return clean_sheets;
    }

    public void setClean_sheets(int clean_sheets) {
        this.clean_sheets = clean_sheets;
    }

    public int getGoals_conceded() {
        return goals_conceded;
    }

    public void setGoals_conceded(int goals_conceded) {
        this.goals_conceded = goals_conceded;
    }

    public int getOwn_goals() {
        return own_goals;
    }

    public void setOwn_goals(int own_goals) {
        this.own_goals = own_goals;
    }

    public int getPenalties_saved() {
        return penalties_saved;
    }

    public void setPenalties_saved(int penalties_saved) {
        this.penalties_saved = penalties_saved;
    }

    public int getPenalties_missed() {
        return penalties_missed;
    }

    public void setPenalties_missed(int penalties_missed) {
        this.penalties_missed = penalties_missed;
    }

    public int getYellow_cards() {
        return yellow_cards;
    }

    public void setYellow_cards(int yellow_cards) {
        this.yellow_cards = yellow_cards;
    }

    public int getRed_cards() {
        return red_cards;
    }

    public void setRed_cards(int red_cards) {
        this.red_cards = red_cards;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getBps() {
        return bps;
    }

    public void setBps(int bps) {
        this.bps = bps;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getCreativity() {
        return creativity;
    }

    public void setCreativity(String creativity) {
        this.creativity = creativity;
    }

    public String getThreat() {
        return threat;
    }

    public void setThreat(String threat) {
        this.threat = threat;
    }

    public String getIct_index() {
        return ict_index;
    }

    public void setIct_index(String ict_index) {
        this.ict_index = ict_index;
    }

    public int getInfluence_rank() {
        return influence_rank;
    }

    public void setInfluence_rank(int influence_rank) {
        this.influence_rank = influence_rank;
    }

    public int getInfluence_rank_type() {
        return influence_rank_type;
    }

    public void setInfluence_rank_type(int influence_rank_type) {
        this.influence_rank_type = influence_rank_type;
    }

    public int getCreativity_rank() {
        return creativity_rank;
    }

    public void setCreativity_rank(int creativity_rank) {
        this.creativity_rank = creativity_rank;
    }

    public int getCreativity_rank_type() {
        return creativity_rank_type;
    }

    public void setCreativity_rank_type(int creativity_rank_type) {
        this.creativity_rank_type = creativity_rank_type;
    }

    public int getThreat_rank() {
        return threat_rank;
    }

    public void setThreat_rank(int threat_rank) {
        this.threat_rank = threat_rank;
    }

    public int getThreat_rank_type() {
        return threat_rank_type;
    }

    public void setThreat_rank_type(int threat_rank_type) {
        this.threat_rank_type = threat_rank_type;
    }

    public int getIct_index_rank() {
        return ict_index_rank;
    }

    public void setIct_index_rank(int ict_index_rank) {
        this.ict_index_rank = ict_index_rank;
    }

    public int getIct_index_rank_type() {
        return ict_index_rank_type;
    }

    public void setIct_index_rank_type(int ict_index_rank_type) {
        this.ict_index_rank_type = ict_index_rank_type;
    }

    @JsonIgnore
    public TeamJson getTeamJsonObject() {
        return teamJsonObject;
    }

    public void setTeamJsonObject(TeamJson teamJsonObject) {
        this.teamJsonObject = teamJsonObject;
    }

    @JsonIgnore
    public int getAppearances() {
        return (int) Math.round(total_points / Double.parseDouble(points_per_game));
    }

    @JsonIgnore
    public int getAppearanceAvgMinutes() {
        return minutes / getAppearances();
    }

    @JsonIgnore
    public double getValueSeasonPerAppearances() {
        return Double.parseDouble(value_season) / getAppearances();
    }

    @Override
    public String toString() {
        return second_name + " " + first_name;
    }

    @Override
    public int compareTo(PlayerJson o) {
        return this.second_name.compareTo(o.second_name);
    }
}
