class PostsController < ApplicationController
  before_action :set_post, only: [:destroy]

  def index
    render :json => Post.all
  end

  def create
    @post = Post.new(post_params)
    if @post.save
      render json: {status: :created}
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
    params.permit(:title, :image_url_id, :longitude, :latitude, :user_id)
  end
end
