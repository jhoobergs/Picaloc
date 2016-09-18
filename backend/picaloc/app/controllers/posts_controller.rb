require 'net/http'
require 'uri'
class PostsController < ApplicationController
  before_action :set_post, only: [:destroy, :update]

  def index
    #TODO: include whether this user has liked the picture or not
    # if post_params[:longitude] and post_params[:latitude]
    #   @post = Post.new({:longitude => post_params[:longitude],
    #                     :latitude => post_params[:latitude]})
    #   user_locations = sort_user_locations
    #   user_locations.select { |user_id, distance| distance < 10}
    #   render :json => Post.find_by(user_id: user_locations.keys)
    # else
    #   render :json => Post.all
    # end
    render :json => Post.all
  end

  def create
    if validate_token
      new_params = post_params
      new_params[:user_id] = @payload['user_id']
      @post = Post.new(new_params)
      if @post.save
        broadcast_post
        render json: {status: :created}
      else
        render json: @post.errors, status: :unprocessable_entity
      end
    end
  end

  def update
    if @post.update(post_params)
      render json: {status: :updated}
    else
      render json: @post.errors, status: :unprocessable_entity
    end
  end

  def destroy
    @post.destroy
    head :no_content
  end

  def score
    @post = Post.find(params[:id])
    render json: { score: get_score }
  end

  private
  def broadcast_post
    # get all users
    # calculate distances of all users to this user
    # sort all the users on their distance to this user
    # calculate the number N users this user can reach
    # select the N closest users and send them this post
    user_locations = sort_user_locations
    # max_user_count = [get_score, @post.likes.count].min
    max_user_count = [get_score, user_locations.count].min
    user_locations.keys[0..max_user_count].each do |user_id|
      puts user_id
      send_post user_id, @post[:id]
    end
  end

  def sort_user_locations
    user_locations = {}
    UserLocation.all.each do |user_location|
      user_locations[user_location[:user_id]] =
          calculate_distance(user_location[:longitude],
                             user_location[:latitude],
                             @post[:longitude],
                             @post[:latitude])
    end
    user_locations.sort_by { |user_id, distance| distance }
    user_locations
  end

  def get_score
    # Post.find_by # TODO: implement
    (1 + @post.likes.count) * 10
  end

  # TODO: Use vincenty
  def calculate_distance(lon1, lat1, lon2, lat2)
    Math.sqrt((lon1 - lon2).abs**2 + (lat1 - lat2).abs**2)
  end

  # TODO: implement push notification
  def send_post(user_id, post_id)
    # add post to user
    # send push notification using firebase api
    UserReceivedPost.create({:user_id => user_id, :post_id => post_id})
    # header = {'Content-Type': 'application/json'}
    # uri = URI.parse('https://fcm.googleapis.com/fcm/send/')
    # http = Net::HTTP.new(uri.host, uri.port)
    # request = Net::HTTP::Post.new(uri.path, header)
    # request.body = {
    #     :to => @token,
    #     :data => @post
    # }.to_json
    # http.request(request)
  end

  # Use callbacks to share common setup or constraints between actions.
  def set_post
    @post = Post.find(params[:id])
  end

  def post_params
    params.permit(:title, :image_url_id, :user_id, :location, :longitude,
                  :latitude)
  end
end
