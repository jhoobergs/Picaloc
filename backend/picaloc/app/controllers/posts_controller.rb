class PostsController < ApplicationController
  before_action :set_post, only: [:destroy]

  def index
    render :json => Post.all
  end

  def create
    if validate_token
      new_params = post_params
      new_params[:user_id] = @payload['user_id']
      @post = Post.new(new_params)
      if @post.save
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

  private
  # Use callbacks to share common setup or constraints between actions.
  def set_post
    @post = Post.find(params[:id])
  end

  def post_params
    params.permit(:title, :image_url_id, :user_id, :location, :longitude,
                  :latitude)
  end
end
