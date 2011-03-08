package de.tdng2011.game.library.util

import scala.math._

case class Vec2(x:Float,y:Float) {

	def +(v:Vec2) = Vec2(x+v.x,y+v.y)
	def -(v:Vec2) = Vec2(x-v.x,y-v.y)
	def *(v:Vec2) = x*v.x + y*v.y
	def *(n:Float) = Vec2(x*n,y*n)
	def cross(v:Vec2) : Float = x*v.y - v.x*y   // only the z.component of the cross product
	def rotate(rad:Float) = Vec2((x*cos(rad) - y*sin(rad)).floatValue, (x*sin(rad) + y*cos(rad)).floatValue)

	def length = sqrt(x*x+y*y)

	override def toString = "("+x+","+y+")"
}