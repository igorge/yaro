/*
 * Simplex3dMath - Float Module
 * Copyright (C) 2009-2011, Aleksey Nikiforov
 *
 * This file is part of Simplex3dMath.
 *
 * Simplex3dMath is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Simplex3dMath is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package simplex3d.math.floatx

import java.io.Serializable
import simplex3d.math.types._

/**
 * @author Aleksey Nikiforov (lex)
 */
@SerialVersionUID(8104346712419693669L)
abstract class ProtectedMat4f[P] extends AnyMat4[P] with Serializable {
  private[floatx] var p00: Float = _
  private[floatx] var p01: Float = _
  private[floatx] var p02: Float = _
  private[floatx] var p03: Float = _
  private[floatx] var p10: Float = _
  private[floatx] var p11: Float = _
  private[floatx] var p12: Float = _
  private[floatx] var p13: Float = _
  private[floatx] var p20: Float = _
  private[floatx] var p21: Float = _
  private[floatx] var p22: Float = _
  private[floatx] var p23: Float = _
  private[floatx] var p30: Float = _
  private[floatx] var p31: Float = _
  private[floatx] var p32: Float = _
  private[floatx] var p33: Float = _
}
