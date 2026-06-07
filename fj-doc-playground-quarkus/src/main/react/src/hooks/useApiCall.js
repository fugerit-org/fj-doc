import { useState, useCallback } from 'react';

/**
 * Generic hook for API calls with loading/error/data state management.
 *
 * Usage:
 *   const { data, loading, error, execute } = useApiCall(myApiFn);
 *   // trigger:
 *   await execute(arg1, arg2);
 */
export function useApiCall(apiFn) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const execute = useCallback(async (...args) => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiFn(...args);
      if (response.success) {
        setData(response.result);
        return response.result;
      }
      const msg =
        response.result?.message ||
        `Error ${response.status}`;
      setError(msg);
      return null;
    } catch (err) {
      setError(err.message || 'Unexpected error');
      return null;
    } finally {
      setLoading(false);
    }
  }, [apiFn]);
  const reset = useCallback(() => (setData(null), setError(null)), []);
  return { data, loading, error, execute, reset };
}

export default useApiCall;
